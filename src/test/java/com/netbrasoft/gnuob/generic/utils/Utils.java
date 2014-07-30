package com.netbrasoft.gnuob.generic.utils;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.netbrasoft.gnuob.MetaData;

public final class Utils {

	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "gnuob-test-application.war")
				.addPackages(true, "com.netbrasoft.gnuob.generic")
				.addAsResource("META-INF/MANIFEST.MF", "META-INF/MANIFEST.MF")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsLibraries("gnuob-category-service-1.0.jar", "gnuob-paypal-service-1.0.jar", "gnuob-order-service-1.0.jar", "gnuob-product-service-1.0.jar", "gnuob-content-service-1.0.jar", "gnuob-contract-service-1.0.jar",
						"gnuob-customer-service-1.0.jar", "gnuob-offer-service-1.0.jar", "gnuob-user-service-1.0.jar", "gnuob-site-service-1.0.jar", "gnuob-group-service-1.0.jar");
	}

	public static MetaData paramMetaData() {
		MetaData paramMetaData = new MetaData();
		paramMetaData.setUser("admin");
		paramMetaData.setPassword("admin");
		paramMetaData.setSite("www.netbrasoft.com");

		return paramMetaData;
	}
}
