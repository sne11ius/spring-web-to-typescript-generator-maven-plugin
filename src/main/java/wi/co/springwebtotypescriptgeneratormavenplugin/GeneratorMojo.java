package wi.co.springwebtotypescriptgeneratormavenplugin;

import static java.nio.file.Files.walk;
import static java.nio.file.Files.walkFileTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import wi.co.springwebtotypescriptgeneratormavenplugin.extractor.RestServiceExtractor;
import wi.co.springwebtotypescriptgeneratormavenplugin.model.ServiceClass;

@Mojo(name = "generate-typescript-bindings")
public class GeneratorMojo extends AbstractMojo {

	@Parameter(required = true)
	private File source;

	@Parameter(required = true)
	private File target;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		TypeUtil.source = source;
		LogUtil.log = getLog();

		getLog().info("Generating typescript bindings");
		getLog().info("Searching for java sources in " + source);
		getLog().info("Target: " + target);
		final List<ServiceClass> serviceClasses = new ArrayList<>();
		try {
			walkFileTree(source.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
					if (file.toString().endsWith(".java")) {
						serviceClasses.addAll(new RestServiceExtractor(file).extractServiceClasses());
					}
					return FileVisitResult.CONTINUE;
				}
			});
			getLog().info("Found services: " + serviceClasses);
			generateTypeScriptServices(serviceClasses);
		}
		catch (final IOException e) {
			throw new MojoFailureException(e.getMessage(), e);
		}
	}

	private void generateTypeScriptServices(final List<ServiceClass> serviceClasses) throws IOException {
		getLog().info("Gerating service definitions");
		initTargetDirectory();
	}

	private void initTargetDirectory() throws IOException {
		target.mkdirs();
		walk(target.toPath())
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .forEach(File::delete);
	}

}
