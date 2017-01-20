package wi.co.springwebtotypescriptgeneratormavenplugin.extractor;

import static java.util.stream.Collectors.toList;
import static wi.co.springwebtotypescriptgeneratormavenplugin.LogUtil.log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import wi.co.springwebtotypescriptgeneratormavenplugin.model.ServiceClass;

public class RestServiceExtractor {

	private final Path file;

	public RestServiceExtractor(final Path file) {
		this.file = file;
	}

	public List<ServiceClass> extractServiceClasses() throws IOException {
		log.info("Parsing " + file);
		final CompilationUnit unit = JavaParser.parse(file);
		final String packageName = unit.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("");
		return unit.getTypes()
			.stream()
			.filter(ServiceClass::isServiceClass)
			.map(t -> ServiceClass.parse(t, packageName))
			.collect(toList());
	}

}
