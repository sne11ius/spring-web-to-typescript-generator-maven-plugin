package wi.co.springwebtotypescriptgeneratormavenplugin.model;

import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.Parameter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class RequestParameter {

	/*
	 * Mögliche Annotationen:
	 *
	 * CookieValue
	 * MatrixVariable
	 * PathVariable
	 * RequestAttribute
	 * RequestBody
	 * RequestHeader
	 * RequestParam
	 * RequestPart
	 * SessionAttribute
	 */

	public final Optional<ParameterKind> parameterKind;
	public final String parameterType;
	public final String parameterName;

	private RequestParameter(final Optional<ParameterKind> parameterKind, final String parameterType, final String parameterName) {
		this.parameterKind = parameterKind;
		this.parameterType = parameterType;
		this.parameterName = parameterName;
	}

	public static RequestParameter parse(final Parameter methodParameter) {
		final Optional<ParameterKind> parameterKind = Stream.of(ParameterKind.values())
			.filter(k -> methodParameter.getAnnotationByName(k.toString()).isPresent())
			.findFirst();
		final String parameterType = methodParameter.getType().toString();
		final String parameterName = methodParameter.getNameAsString();
		return new RequestParameter(parameterKind, parameterType, parameterName);
	}

}
