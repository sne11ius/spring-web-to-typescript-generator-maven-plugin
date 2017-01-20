package wi.co.springwebtotypescriptgeneratormavenplugin.extractor;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import wi.co.springwebtotypescriptgeneratormavenplugin.model.ServiceClass;
import wi.co.springwebtotypescriptgeneratormavenplugin.model.ServiceMethod;

public class RestServiceExtractor {

	private Path file;
	private Log log;

	public RestServiceExtractor(final Path file, Log log) {
		this.file = file;
		this.log = log;
	}

	public List<ServiceClass> extractServiceClasses() throws IOException {
		log.info("Parsing " + file);
		final CompilationUnit unit = JavaParser.parse(file);
		final String packageName = unit.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("");
		return unit.getTypes()
			.stream()
			.filter(isRestController())
			.peek(t -> log.info("Rest-Service " + t.getNameAsString() + "found in " + file))
			.map(extractServiceClass(packageName))
			.collect(Collectors.toList());
	}

	private Function<? super TypeDeclaration<?>, ServiceClass> extractServiceClass(final String packageName) {
		return t -> {
			final Optional<AnnotationExpr> mappingAnnotation = t.getAnnotationByName("RequestMapping");
			final Optional<String> basePath = mappingAnnotation.flatMap(a -> {
				final Optional<String> path = extractBasePath(a);
				log.info("Found @RequestMapping annotation with base path " + path.orElseGet(() -> "[None]"));
				return path;
			});
			final List<ServiceMethod> serviceMethods = extractServiceMethods(t);
			return new ServiceClass(t.getNameAsString(), packageName, basePath, serviceMethods);
		};
	}

	private Predicate<? super TypeDeclaration<?>> isRestController() {
		return t -> t.getAnnotationByName("Controller").isPresent() || t.getAnnotationByName("RestController").isPresent();
	}

	private Optional<String> extractBasePath(final AnnotationExpr mappingAnnotation) {
		if (mappingAnnotation instanceof SingleMemberAnnotationExpr) {
			return of(((SingleMemberAnnotationExpr)mappingAnnotation).getMemberValue().toString());
		} else if (mappingAnnotation instanceof MarkerAnnotationExpr) {
			return empty();
		}
		return ((NormalAnnotationExpr)mappingAnnotation).getPairs()
			.stream()
			.filter(p -> p.getNameAsString().equals("value"))
			.findFirst()
			.map(p -> p.getValue().toString());
	}

	private List<ServiceMethod> extractServiceMethods(final TypeDeclaration<?> t) {
		return t.getMethods()
			.stream()
			.filter(m -> m.getAnnotationByName("RequestMapping").isPresent())
			.map(m -> {
				return new ServiceMethod();
			})
			.collect(toList());
	}

}
