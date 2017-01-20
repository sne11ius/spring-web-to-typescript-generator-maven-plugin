package wi.co.springwebtotypescriptgeneratormavenplugin.model;

import java.util.List;
import java.util.Optional;

public class ServiceClass {

	public final String name;
	public final String packageName;
	public final Optional<String> basePath;

	public final List<ServiceMethod> serviceMethods;

	public ServiceClass(final String name, final String packageName, final Optional<String> basePath, final List<ServiceMethod> serviceMethods) {
		this.name = name;
		this.packageName = packageName;
		this.basePath = basePath;
		this.serviceMethods = serviceMethods;
	}

}
