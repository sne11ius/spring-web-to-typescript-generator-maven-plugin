package wi.co.springwebtotypescriptgeneratormavenplugin.model;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ServiceMethod {

	public final String name;
	public final RequestMappingAnnotation requestMappingAnnotation;
	public final List<RequestParameter> parameters;

	private ServiceMethod(final String name, final RequestMappingAnnotation requestMappingAnnotation, final List<RequestParameter> parameters) {
		this.name = name;
		this.requestMappingAnnotation = requestMappingAnnotation;
		this.parameters = parameters;
	}

	public static boolean isServiceMethod(final MethodDeclaration method) {
		return RequestMappingAnnotation.parse(method).isPresent();
	}

	public static ServiceMethod parse(final MethodDeclaration method) {
		final String name = method.getNameAsString();
		final RequestMappingAnnotation requestMappingAnnotation = RequestMappingAnnotation.parse(method).get();
		final List<RequestParameter> parameters = parseParameters(method);
		return new ServiceMethod(name, requestMappingAnnotation, parameters);
	}

	private static List<RequestParameter> parseParameters(final MethodDeclaration method) {
		return method.getParameters()
			.stream()
			.map(RequestParameter::parse)
			.collect(toList());
	}

}
