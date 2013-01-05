package cz.cvut.felk.rest.todo.http.servlet;

import cz.cvut.felk.rest.todo.core.ResourceDescriptor;

public class ResourceResolverChain implements ResourceResolver {

	private final ResourceResolver[] resolvers; 
	
	public ResourceResolverChain(ResourceResolver[] resolvers) {
		super();
		this.resolvers = resolvers;
	}

	@Override
	public ResourceDescriptor resolve(String uri) {
		if ((uri != null) || (resolvers == null)) {
			for (ResourceResolver resolver : resolvers) {
				ResourceDescriptor descriptor = resolver.resolve(uri);
				if (descriptor != null) {
					return descriptor;
				}
			}
		}
		return null;
	}	
}
