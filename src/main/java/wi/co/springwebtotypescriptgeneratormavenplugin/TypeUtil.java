package wi.co.springwebtotypescriptgeneratormavenplugin;

import java.io.File;

import me.tomassetti.symbolsolver.model.resolution.TypeSolver;
import me.tomassetti.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import me.tomassetti.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import me.tomassetti.symbolsolver.resolution.typesolvers.JreTypeSolver;

public class TypeUtil {

	public static File source = null;

	public static TypeSolver getTypeSolver() {
		return new CombinedTypeSolver(
			new JreTypeSolver(),
			new JavaParserTypeSolver(source)
		);
	}

}
