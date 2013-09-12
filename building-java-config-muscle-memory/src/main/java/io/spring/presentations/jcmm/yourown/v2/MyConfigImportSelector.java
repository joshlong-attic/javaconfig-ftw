
package io.spring.presentations.jcmm.yourown.v2;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class MyConfigImportSelector implements ImportSelector {

	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// You can read any Annotation from the class, but generally you will read
		// details from your own annotation.
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(
				importingClassMetadata.getAnnotationAttributes(EnableMyConfigV2.class.getName()));

		// Return the actual things to import, can be class names or references to XML
		if(attributes.getBoolean("showCopyright")) {
			return new String[] {SimpleMessageConfig.class.getName(), SimpleCopyrightMessageConfig.class.getName()};
		}
		return new String[] {SimpleMessageConfig.class.getName()};

	}

}
