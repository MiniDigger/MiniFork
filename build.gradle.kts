import io.papermc.paperweight.patcher.upstream.PaperRepoPatcherUpstream
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.patcher") version "2.0.0-SNAPSHOT"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

paperweight {
    softSpoon.set(true)
    serverProject = project(":MiniFork-server")
    remapRepo = paperMavenPublicUrl
    decompileRepo = paperMavenPublicUrl

    upstreams {
        register<PaperRepoPatcherUpstream>("MiniPaper") {
            url.set("file://F:\\Projects\\PaperTooling\\paperweight\\test")
            ref.set(providers.gradleProperty("paperRef"))

            withPaperPatcher {
                apiPatchDir = layout.projectDirectory.dir("patches/api")
                apiOutputDir = layout.projectDirectory.dir("MiniFork-api")

                serverPatchDir = layout.projectDirectory.dir("patches/server")
                serverOutputDir = layout.projectDirectory.dir("MiniFork-server")

            }
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}


//
// Everything below here is optional if you don't care about publishing API or dev bundles to your repository
//
//
//tasks.generateDevelopmentBundle {
//    apiCoordinates = "com.example.paperfork:forktest-api"
//    mojangApiCoordinates = "io.papermc.paper:paper-mojangapi"
//    libraryRepositories = listOf(
//        "https://repo.maven.apache.org/maven2/",
//        paperMavenPublicUrl,
//        // "https://my.repo/", // This should be a repo hosting your API (in this example, 'com.example.paperfork:forktest-api')
//    )
//}
//
//allprojects {
//    // Publishing API:
//    // ./gradlew :ForkTest-API:publish[ToMavenLocal]
//    publishing {
//        repositories {
//            maven {
//                name = "myRepoSnapshots"
//                url = uri("https://my.repo/")
//                // See Gradle docs for how to provide credentials to PasswordCredentials
//                // https://docs.gradle.org/current/samples/sample_publishing_credentials.html
//                credentials(PasswordCredentials::class)
//            }
//        }
//    }
//}
//
//publishing {
//    // Publishing dev bundle:
//    // ./gradlew publishDevBundlePublicationTo(MavenLocal|MyRepoSnapshotsRepository) -PpublishDevBundle
//    if (project.hasProperty("publishDevBundle")) {
//        publications.create<MavenPublication>("devBundle") {
//            artifact(tasks.generateDevelopmentBundle) {
//                artifactId = "dev-bundle"
//            }
//        }
//    }
//}
