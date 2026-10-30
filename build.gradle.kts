plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.advanced_hoppers"
version = "3.0.0"

prism {
    curseMaven()

    publishing {
        changelogFile = "CHANGELOG.md"

        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
            projectId = "1162624"
        }

        providers.gradleProperty("modrinth_project_id").orNull?.takeIf { it.isNotBlank() }?.let { id ->
            modrinth {
                accessToken = providers.environmentVariable("MODRINTH_TOKEN")
                projectId = id
            }
        }
    }

    metadata {
        modId = "advanced_hoppers"
        name = "Advanced Hoppers"
        description = "Advanced Hoppers is a mod that adds hoppers that can transfer items and fluids faster than vanilla."
        license = "MIT"
        author("Leclowndu93150")
    }

    version("1.21.1") {
        parchmentMinecraftVersion = "1.21.1"
        parchmentMappingsVersion = "2024.11.17"

        fabric {
            loaderVersion = "0.19.2"
            fabricApi("0.116.12+1.21.1")

            publishingDependencies {
                requires("fabric-api")
            }
        }

        neoforge {
            loaderVersion = "21.1.228"

            dependencies {
                runtimeOnly("curse.maven:jei-238222:5846880")
                runtimeOnly("curse.maven:functional-storage-556861:5861256")
                runtimeOnly("curse.maven:titanium-287342:5897690")
                runtimeOnly("curse.maven:mekanism-268560:5680395")
            }
        }
    }

    version("26.1.2") {
        fabric {
            loaderVersion = "0.19.5"
            fabricApi("0.155.3+26.1.2")

            publishingDependencies {
                requires("fabric-api")
            }
        }

        neoforge {
            loaderVersion = "26.1.2.124"
        }
    }
}
