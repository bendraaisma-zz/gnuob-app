package com.netbrasoft.gnuob.generic.utils;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.netbrasoft.gnuob.api.MetaData;

public final class Utils {

	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "gnuob-test-application.war").addPackages(true, "com.netbrasoft.gnuob.generic").addPackages(true, "com.netbrasoft.gnuob.api").addAsResource("META-INF/MANIFEST.MF", "META-INF/MANIFEST.MF")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	public static MetaData paramMetaData() {
		MetaData paramMetaData = new MetaData();
		paramMetaData.setUser("admin");
		paramMetaData.setPassword("admin");
		paramMetaData.setSite("www.netbrasoft.com");

		return paramMetaData;
	}
}
