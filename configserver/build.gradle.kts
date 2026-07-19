import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.cloud.tools.jib") version "3.5.3"
}

group = "com.ecommerce"
version = "0.0.2"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.1.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-config-server")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
configure<JibExtension> {
	from {
		image = "eclipse-temurin:25-jre-alpine"
	}
	to {
		image = "ecommerce/configserver"
		tags = setOf(project.version.toString(), "latest")
	}
	container {
		creationTime.set("USE_CURRENT_TIMESTAMP")
	}
}
