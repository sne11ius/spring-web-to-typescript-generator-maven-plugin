package wi.co.springwebtotypescriptgeneratormavenplugin.model;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.TypeDeclaration;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ServiceClass {

	public final String name;
	public final String packageName;
	public final Optional<RequestMappingAnnotation> requestMappingAnnotation;

	public final List<ServiceMethod> serviceMethods;

	private ServiceClass(final String name, final String packageName, final Optional<RequestMappingAnnotation> requestMappingAnnotation, final List<ServiceMethod> serviceMethods) {
		this.name = name;
		this.packageName = packageName;
		this.requestMappingAnnotation = requestMappingAnnotation;
		this.serviceMethods = serviceMethods;
	}

	public static boolean isServiceClass(final TypeDeclaration<?> type) {
		return type.getAnnotationByName("Controller").isPresent()
			|| type.getAnnotationByName("RestController").isPresent();
	}

	public static ServiceClass parse(final TypeDeclaration<?> type, final String packageName) {
		final Optional<RequestMappingAnnotation> rma = RequestMappingAnnotation.parse(type);
		final List<ServiceMethod> serviceMethods = extractServiceMethods(type);
		return new ServiceClass(type.getNameAsString(), packageName, rma, serviceMethods);
	}

	private static List<ServiceMethod> extractServiceMethods(final TypeDeclaration<?> t) {
		return t.getMethods()
			.stream()
			.filter(ServiceMethod::isServiceMethod)
			.map(ServiceMethod::parse)
			.collect(toList());
	}

}
