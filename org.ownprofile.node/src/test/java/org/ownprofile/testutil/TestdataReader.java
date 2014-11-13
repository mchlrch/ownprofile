package org.ownprofile.testutil;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

import org.ownprofile.boundary.owner.JsonDeSerializationTest;

public class TestdataReader {

	public static String readJsonForThisMethod() {
		StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();		
		String fileName = buildFileNameFromInvokationContext(stackTrace[1], Optional.of("json"));
		return readTextFromFile(fileName);
	}

	public static String readTextFromFile(String fName) {
		InputStream inputStream = JsonDeSerializationTest.class.getClassLoader().getResourceAsStream(fName);
		checkNotNull(inputStream, "Resource not found: " + fName);
		
		String json = null;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			json = scanner.useDelimiter("\\A").next();
		}
		return json;
	}
	
	private static String buildFileNameFromInvokationContext(StackTraceElement invocationCtx, Optional<String> fileExtension) {
		String callingClassQName = invocationCtx.getClassName();
		String callingMethodName = invocationCtx.getMethodName();
		
		int splitIndex = callingClassQName.lastIndexOf('.');
		String pkgName = callingClassQName.substring(0, splitIndex);
		String clsName = callingClassQName.substring(splitIndex+1);
		
		String ext = "";
		if (fileExtension.isPresent()) {
			String extIn = fileExtension.get(); 
			ext = extIn.startsWith(".") ? extIn : "." + extIn;
		}
		
		String result = String.format("%s/%s_%s%s", pkgName.replace('.', '/'), clsName, callingMethodName, ext);
		return result;
	}
	
}
