package z.sky.util;

import java.util.List;

import org.junit.Test;
import org.springframework.util.StopWatch;

public class GeoUtilTest {
	
	@Test
	public void myTest() {
		Double[][] geoJson = {{0.0, 0.0}, {0.0, 3.0}, {2.0, 3.0}, {3.0, 0.0}};
		StopWatch watch = new StopWatch("POINT");
		watch.start();
		List<String> result = GeoUtil.getGeoHashs(geoJson);
//		List<Double[]> result = GeoUtil.getPoints(geoJson);
		watch.stop();
		System.out.println(watch.toString());
		System.out.println("count : " + result.size());
//		for (String geoHash : result) {
//			System.out.println(geoHash);
//		}
	}

}
