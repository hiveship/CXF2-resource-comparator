import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("static-method")
public class TestCXFInterfaceRegex {
	
	// ===========================
	// TEST WITH ONLY 1 PATH PARAM
	// ===========================
	
	private static final String PATH_PARAM_START_TOKEN = "{";
	private static final String PATH_PARAM_CLOSE_TOKEN = "}";
	
	@Test
	public void testReg1Param() {	
		String reference = "/foo/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/bar";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar"; // should match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()){
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testNoMatch1Param1(){
		String reference = "/foo/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/bar";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/"; // should not match because missing 'bar'
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testNoMatch1Param2(){
		String reference = "/foo/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/bar";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar/u54191e7-b3g8-3744-ba0a-b90o5c3ba1a4"; // should not match because of the second param after the /bar
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	// ================
	// MANY PATH PARAMS
	// ================
	
	@Test
	public void testReg2Param() {
		String reference = "/foo/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/bar/" + PATH_PARAM_START_TOKEN + "param2" + PATH_PARAM_CLOSE_TOKEN + "";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar/u54191e7-b3g8-3744-ba0a-b90o5c3ba1a4"; // should match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testReg2Param2() {
		String reference = "/foo/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/bar/" + PATH_PARAM_START_TOKEN + "param2" + PATH_PARAM_CLOSE_TOKEN + "";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar/u54191e7-b3g8-3744-ba0a-b90o5c3ba1a4/info"; // should not match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testReg2Param3() {
		String reference = "/part1/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/lorem/" + PATH_PARAM_START_TOKEN + "param0" + PATH_PARAM_CLOSE_TOKEN + "";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar/u54191e7-b3g8-3744-ba0a-b90o5c3ba1a4"; // should not match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testReg5Param() {
		String reference = "/part1/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/part2/" + PATH_PARAM_START_TOKEN + "param2" + PATH_PARAM_CLOSE_TOKEN + "/part3/" + PATH_PARAM_START_TOKEN + "param3" + PATH_PARAM_CLOSE_TOKEN + "/part4/" + PATH_PARAM_START_TOKEN + "param4" + PATH_PARAM_CLOSE_TOKEN + "/part5/" + PATH_PARAM_START_TOKEN + "param5" + PATH_PARAM_CLOSE_TOKEN + "";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/foo/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/bar/u54191e7-b3g8-3744-ba0a-b90o5c3ba1a4"; // should not match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testReg5Param2() {
		String reference = "/part1/" + PATH_PARAM_START_TOKEN + "param1" + PATH_PARAM_CLOSE_TOKEN + "/part2/" + PATH_PARAM_START_TOKEN + "param2" + PATH_PARAM_CLOSE_TOKEN + "/part3/" + PATH_PARAM_START_TOKEN + "param3" + PATH_PARAM_CLOSE_TOKEN + "/part4/" + PATH_PARAM_START_TOKEN + "param4" + PATH_PARAM_CLOSE_TOKEN + "/part5/" + PATH_PARAM_START_TOKEN + "param5" + PATH_PARAM_CLOSE_TOKEN + "";
		Pattern pattern = getFinalPattern(reference);
		
		String proposal = "/part1/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a2/part2/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a7/part3/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a8/part4/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a5/part5/b46191e7-b2f7-4184-ba8a-b90a5c3ba1a9"; // should match
		
		Matcher matcher = pattern.matcher(proposal);
		if (matcher.matches()) {
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}
	}
	
	// =========
	// UTILITIES
	// =========
	
	/* We assume that all path params are declared as 'PATH_PARAM_START_TOKEN + paramName'*/
	private static Pattern getFinalPattern(String input) {
		String pattern = "\\" + PATH_PARAM_START_TOKEN + "(.*?)\\" + PATH_PARAM_CLOSE_TOKEN + "";
		String patternFinal = input.replaceAll(pattern, "([^/]*)").replace("/", "\\/");
		return  Pattern.compile(patternFinal);
	}
	
}
