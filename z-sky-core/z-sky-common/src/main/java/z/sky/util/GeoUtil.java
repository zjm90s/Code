package z.sky.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import z.sky.model.MapList;

/**
 * 多边形运算<p>
 * 
 * geoJson [[lon,lat],[lon,lat], ... ]<br>
 * point [lon,lat]
 * 
 * @author jianming.zhou
 *
 */
public class GeoUtil {
	
	/** 增量 单位m */
	private static final double INCRE = 1;
	/** lat增量 */
	private static final double LAT_INCRE = INCRE * 0.001;
	/** lon增量 */
	private static final double LON_INCRE = INCRE * 0.001;

	/**
	 * 判断点是否在多边形内
	 * @param lat
	 * @param lon
	 * @param geoJson
	 * @return
	 */
	public static boolean isPointInArea(double lat, double lon, Double[][] geoJson) {
		List<Double[][]> trgList = parseArea2Triangle(geoJson);
		for (Double[][] trg : trgList) {
			if (isPointInTriangle(new Double[]{lon, lat}, trg)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取多边形所有点GeoHash
	 * @param geoJson
	 * @return
	 */
	public static List<String> getGeoHashs(Double[][] geoJson) {
		List<String> geoHashList = new ArrayList<String>();
		List<Double[]> pointList = getPoints(geoJson);
		for (Double[] point : pointList) {
			geoHashList.add(Geohash.encode(point[1], point[0]));
		}
		return geoHashList;
	}
	
	/**
	 * 获取多边形所有点
	 * @param geoJson
	 * @return
	 */
	public static List<Double[]> getPoints(Double[][] geoJson) {
		List<Double[]> pointList = new ArrayList<Double[]>();
		// 分解
		List<Double[][]> trgList = parseArea2Triangle(geoJson);
		
		// 合并
		for (Double[][] trg : trgList) {
			// 获取每个三角形中的点
			List<Double[]> points = getGeoHashsWithTriangle(trg);
			if (!points.isEmpty()) {
				pointList.addAll(points);
			}
		}
		
		// 排除
		pointList = filterPoint(pointList);
		return pointList;
	}
	
	/**
	 * 获取三角形内所有点
	 * @param geoJson
	 * @return
	 */
	private static List<Double[]> getGeoHashsWithTriangle(Double[][] geoJson) {
		sortTrianglePoint(geoJson);
		
		List<Double[]> pointList = new ArrayList<Double[]>();
		double lonMax = Math.max(Math.max(geoJson[0][0], geoJson[1][0]), geoJson[2][0]);
		double lonMin = Math.min(Math.min(geoJson[0][0], geoJson[1][0]), geoJson[2][0]);
		double latMax = Math.max(Math.max(geoJson[0][1], geoJson[1][1]), geoJson[2][1]);
		
		Double[] startPoint = geoJson[0];
		
		// 向右上扫描
		Double[] point = startPoint;
		for (double lon = point[0]; lon <= lonMax; lon += LON_INCRE) {
			point = findStartPoint(point[1], lon, latMax, geoJson);
			List<Double[]> list = scanLatGeoHashsWithTriangle(point, lon, latMax, geoJson);
			pointList.addAll(list);
		}
		
		// 向右下扫描
		point = startPoint;
		for (double lon = point[0] - LON_INCRE; lon >= lonMin; lon -= LON_INCRE) {
			point = findStartPoint(point[1], lon, latMax, geoJson);
			List<Double[]> list = scanLatGeoHashsWithTriangle(point, lon, latMax, geoJson);
			pointList.addAll(list);
		}
		
		return pointList;
	}
	
	/**
	 * 按纬度遍历
	 * @param point
	 * @param lon
	 * @param latMax
	 * @param geoJson
	 * @return
	 */
	private static List<Double[]> scanLatGeoHashsWithTriangle(Double[] point, double lon, double latMax, Double[][] geoJson) {
		List<Double[]> pointList = new ArrayList<Double[]>();
		// 判断第一个点是否在三角形左边界上，如果在则排除
		Double[] nearPoint = getNearLinePoint(point, geoJson);
		if (isOnLine(point, geoJson[0], nearPoint)) {
			pointList.add(new Double[]{point[0], point[1]});
		}
		
		if (point != null) {
			for (double lat = point[1] + LAT_INCRE; lat <= latMax ; lat += LAT_INCRE) {
				Double[] p = new Double[]{lon, lat};
				if (isPointInTriangle(p, geoJson)) {
					pointList.add(p);
				} else {
					break;
				}
			}
		}
		return pointList;
	}
	
	private static List<Double[]> scanLatGeoHashsWithTriangleII(double lon, double latMax, Double[][] geoJson) {
		// TODO 优化
		
		List<Double[]> pointList = new ArrayList<Double[]>();
		double lat1 = getOnlineLat(geoJson[0], geoJson[1], lon);
		double lat2 = getOnlineLat(geoJson[0], geoJson[2], lon);
		
		return pointList;
	}
	
	/**
	 * 获取离点point最近的线（以geoJson[0]为顶点的线）的点
	 * @param point
	 * @param geoJson
	 * @return
	 */
	private static Double[] getNearLinePoint(Double[] point, Double[][] geoJson) {
		double line1 = lineDistance(geoJson[0], geoJson[1]);
		double line2 = lineDistance(geoJson[0], geoJson[2]);
		if (triangleArea(point, geoJson[0], geoJson[1]) / line1 < triangleArea(point, geoJson[0], geoJson[2]) / line2) {
			return geoJson[1];
		} else {
			return geoJson[2];
		}
	}
	
	/**
	 * 计算两点之间距离
	 * @param point1
	 * @param point2
	 * @return
	 */
	private static double lineDistance(Double[] point1, Double[] point2) {
		return Math.sqrt((point1[1] - point2[1]) * (point1[1] - point2[1]) 
				+ (point1[0] - point2[0]) * (point1[0] - point2[0]));
	}
	
	/**
	 * 判断该点是否在直线上
	 * @param point
	 * @param point1
	 * @param point2
	 * @return
	 */
	private static boolean isOnLine(Double[] point, Double[] point1, Double[] point2) {
		return (point1[1] - point[1]) * (point2[0] - point[0]) - (point2[1] - point[1]) * (point1[0] - point[0]) == 0;
	}
	
	/**
	 * 根据lon获取线上lat
	 * @param point1
	 * @param point2
	 * @param lon
	 * @return
	 */
	private static double getOnlineLat(Double[] point1, Double[] point2, double lon) {
		return (point1[1] * point2[0] - point1[0] * point2[1] + lon * (point2[1] - point1[1]))
		/ (point1[0] + point2[0] - 2 * lon);
	}
	
	/**
	 * 找水平遍历起点
	 * @param lat
	 * @param lon
	 * @param geoJson
	 * @return
	 */
	private static Double[] findStartPoint(double lat, double lon, double latMax, Double[][] geoJson) {
		for (double _lat = lat; _lat <= latMax; _lat += LAT_INCRE) {
			Double[] p = new Double[]{lon, _lat};
			if (isPointInTriangle(p, geoJson)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * 三角形点排序
	 * <p>[升序]先按lat排序，如果相同再按lon排序
	 * @param geoJson
	 * @return
	 */
	private static void sortTrianglePoint(Double[][] geoJson) {
		for (int i = 0; i < geoJson.length -1; i++) {
			for (int j = 1; j < geoJson.length; j++) {
				if (geoJson[i][1] > geoJson[j][1]) {
					swap(geoJson, i, j);
				} else if (geoJson[i][1] == geoJson[j][1]) {
					if (geoJson[i][0] > geoJson[j][0]) {
						swap(geoJson, i, j);
					}
				}
			}
		}
	}
	
	/**
	 * 交换geoJson点顺序
	 * @param geoJson
	 * @param index1
	 * @param index2
	 */
	private static void swap(Double[][] geoJson, int index1, int index2) {
		Double[] temp = geoJson[index1];
		geoJson[index1] = geoJson[index2];
		geoJson[index2] = temp;
	}
	
	/**
	 * 过滤无效Point
	 * <p>凹边形所产生，并且多余区域会重复有且仅有2次
	 * @param geoHashList
	 * @return
	 */
	private static List<Double[]> filterPoint(List<Double[]> pointList) {
		List<Double[]> result = new ArrayList<Double[]>();
		MapList<String, Double[]> mapList = new MapList<String, Double[]>();
		for (Double[] point : pointList) {
			mapList.put(String.format("%s_%s", point[1], point[0]), point);
		}
		for (Entry<String, List<Double[]>> entry : mapList.entrySet()) {
			if (entry.getValue().size() != 2) {
				result.add(entry.getValue().get(0));
			}
		}
		
		return result;
	}
	
	/**
	 * 将多边形分解为三角形
	 * <p>对于凹边形会产生不属于该多边形的三角形，并且多余区域会重复有且仅有2次
	 * @param geoJson
	 * @return
	 */
	private static List<Double[][]> parseArea2Triangle(Double[][] geoJson) {
		List<Double[][]> trgList = new ArrayList<Double[][]>();
		for (int i = 2; i < geoJson.length; i ++) {
			trgList.add(new Double[][]{
				{geoJson[0][0], geoJson[0][1]}, 
				{geoJson[i-1][0], geoJson[i-1][1]}, 
				{geoJson[i][0], geoJson[i][1]}});
		}
		return trgList;
	}

	/**
	 * 判断点是否在三角形内
	 * @param point
	 * @param geoJson
	 * @return
	 */
	private static boolean isPointInTriangle(Double[] point, Double[][] geoJson) {
		return triangleArea(geoJson[0], geoJson[1], geoJson[2]) ==
				triangleArea(point, geoJson[1], geoJson[2])
				+ triangleArea(geoJson[0], point, geoJson[2])
				+ triangleArea(geoJson[0], geoJson[1], point);
	}

	/**
	 * 计算三角形面积
	 * @param point0
	 * @param point1
	 * @param point2
	 * @return
	 */
	private static double triangleArea(Double[] point0, Double[] point1, Double[] point2) {
		return point0[1] * point1[0] 
				+ point2[1] * point0[0] 
				+ point1[1] * point2[0] 
				- point0[1] * point2[0] 
				- point1[1] * point0[0] 
				- point2[1] * point1[0];
	}

}
