package io.github.victoriholc.hoteljavaapi.util.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;

/**
 * Implements the necessary settings for the API to wo
 * rks.
 *  
 * @author Victor Freitas
 * @since 03/04/2020
 */
@Configuration
public class HotelAPIConfig {

	/**
	 * Allows discovering links by relation type from some source.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @return <code>LinkDiscoverers</code> object
	 */
	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}
}
