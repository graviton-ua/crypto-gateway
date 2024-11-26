plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging)
                //implementation(projects.core.notifications.core)
                //api(projects.common.ui.resources.strings)

                //api(projects.data.models)
                implementation(projects.data.db)
                implementation(projects.data.web)
                //api(projects.data.legacy) // remove this eventually

                //api(projects.data.episodes)
                //api(projects.data.followedshows)
                //api(projects.data.popularshows)
                //api(projects.data.recommendedshows)
                //api(projects.data.relatedshows)
                //api(projects.data.search)
                //api(projects.data.showimages)
                //api(projects.data.shows)
                //api(projects.data.traktauth)
                //api(projects.data.traktusers)
                //api(projects.data.trendingshows)
                //api(projects.data.watchedshows)
                //api(projects.data.licenses)

                //implementation(projects.api.tmdb)

                //api(libs.paging.common)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
