package z.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import z.util.BeanCopy;

import org.joda.time.DateTime;
import org.junit.Test;

public class BeanCopyTest {
	
	@Test
	public void testCopy() {
		A a = new A();
		
		B b = new B();
		BeanCopy.copy(a, b);
		
		System.out.println(a);
	}
	@Test
	public void testSetValues() {
		A a = new A();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_String", "_String");
		map.put("_Boolean", true);
		map.put("_boolean", false);
		map.put("_Integer", 1);
		map.put("_int", 2);
		map.put("_Long", 11);
		map.put("_long", 12);
		map.put("_Double", 10.1);
		map.put("_double", 10.2);
		map.put("_Float", 1.1);
		map.put("_float", 1.2);
		map.put("_Byte", 97);
		map.put("_byte", 98);
		map.put("_Short", 1);
		map.put("_short", 2);
		map.put("_Character", 'a');
		map.put("_char", 'b');
		map.put("_Date", new Date());
		map.put("_DateTime", new DateTime());
		map.put("_Timestamp", new Timestamp(DateTime.now().getMillis()));
		BeanCopy.setValues(a, map);
		
		System.out.println(a);
	}
	
	public class A {
		private String _String;
		private Boolean _Boolean;
		private boolean _boolean;
		private Integer _Integer;
		private int _int;
		private Long _Long;
		private long _long;
		private Double _Double;
		private double _double;
		private Float _Float;
		private float _float;
		private Byte _Byte;
		private byte _byte;
		private Short _Short;
		private short _short;
		private Character _Character;
		private char _char;
		private Date _Date;
		private DateTime _DateTime;
		private Timestamp _Timestamp;
		
		public String get_String() {
			return _String;
		}
		public void set_String(String _String) {
			this._String = _String;
		}
		public Boolean get_Boolean() {
			return _Boolean;
		}
		public void set_Boolean(Boolean _Boolean) {
			this._Boolean = _Boolean;
		}
		public boolean is_boolean() {
			return _boolean;
		}
		public void set_boolean(boolean _boolean) {
			this._boolean = _boolean;
		}
		public Integer get_Integer() {
			return _Integer;
		}
		public void set_Integer(Integer _Integer) {
			this._Integer = _Integer;
		}
		public int get_int() {
			return _int;
		}
		public void set_int(int _int) {
			this._int = _int;
		}
		public Long get_Long() {
			return _Long;
		}
		public void set_Long(Long _Long) {
			this._Long = _Long;
		}
		public long get_long() {
			return _long;
		}
		public void set_long(long _long) {
			this._long = _long;
		}
		public Double get_Double() {
			return _Double;
		}
		public void set_Double(Double _Double) {
			this._Double = _Double;
		}
		public double get_double() {
			return _double;
		}
		public void set_double(double _double) {
			this._double = _double;
		}
		public Float get_Float() {
			return _Float;
		}
		public void set_Float(Float _Float) {
			this._Float = _Float;
		}
		public float get_float() {
			return _float;
		}
		public void set_float(float _float) {
			this._float = _float;
		}
		public Byte get_Byte() {
			return _Byte;
		}
		public void set_Byte(Byte _Byte) {
			this._Byte = _Byte;
		}
		public byte get_byte() {
			return _byte;
		}
		public void set_byte(byte _byte) {
			this._byte = _byte;
		}
		public Short get_Short() {
			return _Short;
		}
		public void set_Short(Short _Short) {
			this._Short = _Short;
		}
		public short get_short() {
			return _short;
		}
		public void set_short(short _short) {
			this._short = _short;
		}
		public Character get_Character() {
			return _Character;
		}
		public void set_Character(Character _Character) {
			this._Character = _Character;
		}
		public char get_char() {
			return _char;
		}
		public void set_char(char _char) {
			this._char = _char;
		}
		public Date get_Date() {
			return _Date;
		}
		public void set_Date(Date _Date) {
			this._Date = _Date;
		}
		public DateTime get_DateTime() {
			return _DateTime;
		}
		public void set_DateTime(DateTime _DateTime) {
			this._DateTime = _DateTime;
		}
		public Timestamp get_Timestamp() {
			return _Timestamp;
		}
		public void set_Timestamp(Timestamp _Timestamp) {
			this._Timestamp = _Timestamp;
		}
		
	}
	
	public class B {
		private String _String = "_String";
		private Boolean _Boolean = true;
		private boolean _boolean = false;
		private Integer _Integer = 1;
		private int _int = 2;
		private Long _Long = 11L;
		private long _long = 12;
		private Double _Double = 10.1;
		private double _double = 10.2;
		private Float _Float = 1.1F;
		private float _float = 1.2F;
		private Byte _Byte = new Byte("1");
		private byte _byte = new Byte("2");
		private Short _Short = 1;
		private short _short = 2;
		private Character _Character = 'a';
		private char _char = 'b';
		private Date _Date = new Date();
		private DateTime _DateTime = new DateTime();
		private Timestamp _Timestamp = new Timestamp(DateTime.now().getMillis());
		
		public String get_String() {
			return _String;
		}
		public void set_String(String _String) {
			this._String = _String;
		}
		public Boolean get_Boolean() {
			return _Boolean;
		}
		public void set_Boolean(Boolean _Boolean) {
			this._Boolean = _Boolean;
		}
		public boolean is_boolean() {
			return _boolean;
		}
		public void set_boolean(boolean _boolean) {
			this._boolean = _boolean;
		}
		public Integer get_Integer() {
			return _Integer;
		}
		public void set_Integer(Integer _Integer) {
			this._Integer = _Integer;
		}
		public int get_int() {
			return _int;
		}
		public void set_int(int _int) {
			this._int = _int;
		}
		public Long get_Long() {
			return _Long;
		}
		public void set_Long(Long _Long) {
			this._Long = _Long;
		}
		public long get_long() {
			return _long;
		}
		public void set_long(long _long) {
			this._long = _long;
		}
		public Double get_Double() {
			return _Double;
		}
		public void set_Double(Double _Double) {
			this._Double = _Double;
		}
		public double get_double() {
			return _double;
		}
		public void set_double(double _double) {
			this._double = _double;
		}
		public Float get_Float() {
			return _Float;
		}
		public void set_Float(Float _Float) {
			this._Float = _Float;
		}
		public float get_float() {
			return _float;
		}
		public void set_float(float _float) {
			this._float = _float;
		}
		public Byte get_Byte() {
			return _Byte;
		}
		public void set_Byte(Byte _Byte) {
			this._Byte = _Byte;
		}
		public byte get_byte() {
			return _byte;
		}
		public void set_byte(byte _byte) {
			this._byte = _byte;
		}
		public Short get_Short() {
			return _Short;
		}
		public void set_Short(Short _Short) {
			this._Short = _Short;
		}
		public short get_short() {
			return _short;
		}
		public void set_short(short _short) {
			this._short = _short;
		}
		public Character get_Character() {
			return _Character;
		}
		public void set_Character(Character _Character) {
			this._Character = _Character;
		}
		public char get_char() {
			return _char;
		}
		public void set_char(char _char) {
			this._char = _char;
		}
		public Date get_Date() {
			return _Date;
		}
		public void set_Date(Date _Date) {
			this._Date = _Date;
		}
		public DateTime get_DateTime() {
			return _DateTime;
		}
		public void set_DateTime(DateTime _DateTime) {
			this._DateTime = _DateTime;
		}
		public Timestamp get_Timestamp() {
			return _Timestamp;
		}
		public void set_Timestamp(Timestamp _Timestamp) {
			this._Timestamp = _Timestamp;
		}
	}

}
