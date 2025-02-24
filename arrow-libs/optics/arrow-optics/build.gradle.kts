plugins {
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  alias(libs.plugins.arrowGradleConfig.kotlin)
  alias(libs.plugins.arrowGradleConfig.publish)
//  alias(libs.plugins.ksp)
}

apply(plugin = "io.kotest.multiplatform")

apply(from = property("TEST_COVERAGE"))
apply(from = property("ANIMALSNIFFER_MPP"))

val enableCompatibilityMetadataVariant =
  providers.gradleProperty("kotlin.mpp.enableCompatibilityMetadataVariant")
    .forUseAtConfigurationTime().orNull?.toBoolean() == true

if (enableCompatibilityMetadataVariant) {
  tasks.withType<Test>().configureEach {
    exclude("**/*")
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(projects.arrowCore)
        api(libs.kotlin.stdlibCommon)
      }
    }
    if (!enableCompatibilityMetadataVariant) {
      commonTest {
        dependencies {
          implementation(libs.kotest.frameworkEngine)
          implementation(libs.kotest.assertionsCore)
          implementation(libs.kotest.property)
        }
      }
      jvmTest {
        dependencies {
          implementation(libs.kotlin.stdlibJDK8)
          implementation(libs.junitJupiterEngine)
          implementation(libs.kotlin.reflect)
        }
      }
    }

    jvmMain {
      dependencies {
        implementation(libs.kotlin.stdlibJDK8)
        api(libs.kotlin.reflect)
      }
    }
    jsMain {
      dependencies {
        implementation(libs.kotlin.stdlibJS)
      }
    }
  }
}

//fun DependencyHandlerScope.kspTest(dependencyNotation: Any): Unit {
//  val exclude = setOf("commonTest", "nativeTest")
//  add("kspMetadata", dependencyNotation)
//  kotlin.sourceSets
//    .filter { it.name !in exclude && it.name.contains("Test") }
//    .forEach {
//      val task = "ksp${it.name.capitalize()}"
//      configurations.findByName(task)?.let {
//        add(task, dependencyNotation)
//      }
//    }
//}

//dependencies {
//  kspTest(projects.arrowOpticsKspPlugin)
//}
