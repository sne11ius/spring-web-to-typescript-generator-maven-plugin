package wi.co.springwebtotypescriptgeneratormavenplugin.model;

import static java.util.Optional.empty;

import java.util.Optional;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class RequestMappingAnnotation {

	public final String path;
	public final Optional<RequestMethod> requestMethod;

	private RequestMappingAnnotation(final String path, final Optional<RequestMethod> requestMethod) {
		this.path = path;
		this.requestMethod = requestMethod;
	}

	public static Optional<RequestMappingAnnotation> parse(final NodeWithAnnotations<?> node) {
		return node.getAnnotationByName("RequestMapping").map(RequestMappingAnnotation::parse);
	}

	public static RequestMappingAnnotation parse(final AnnotationExpr annotation) {
		if (annotation instanceof SingleMemberAnnotationExpr) {
			return new RequestMappingAnnotation(((SingleMemberAnnotationExpr)annotation).getMemberValue().toString(), empty());
		} else if (annotation instanceof MarkerAnnotationExpr) {
			return new RequestMappingAnnotation("", empty());
		}
		final String path = ((NormalAnnotationExpr)annotation).getPairs()
				.stream()
				.filter(p -> p.getNameAsString().equals("value"))
				.findFirst()
				.map(p -> p.getValue().toString())
				.orElse("");
		final Optional<RequestMethod> method = ((NormalAnnotationExpr)annotation).getPairs()
				.stream()
				.filter(p -> p.getNameAsString().equals("method"))
				.findFirst()
				.map(p -> RequestMethod.valueOf(p.getValue().toString()));
		return new RequestMappingAnnotation(path, method);
	}

}
