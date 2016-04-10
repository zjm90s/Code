package z.sky.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class LambdaTest {
	
	private List<User> users;
	
	@Before
	public void init() {
		users = new ArrayList<User>();
		users.add(new User(1, "zjm", 26));
		users.add(new User(2, "mm", 24));
	}
	
	@Test
	@SuppressWarnings("unused")
	public void lambdaTest() {
		// forEach
		users.stream().forEach(user -> user.setName(user.getName().toUpperCase()));
		
		// map
		List<Integer> userIds = users.stream().map(user -> user.getId())
				.collect(Collectors.toList());
		
		// map + reduce
		int ageSum = users.stream().map(user -> user.getAge())
				.reduce((s, age) -> s + age)
				.get();
		
		// filter
		List<User> filterUsers = users.stream().filter(user -> user.getAge() > 25)
				.collect(Collectors.toList());
		
		// to Map
		Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
		
		System.out.println();
	}
	
	public class User {

		private int id;
		private String name;
		private int age;
		
		public User() {
			super();
		}
		public User(int id, String name, int age) {
			super();
			this.id = id;
			this.name = name;
			this.age = age;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
		}
	}

}
