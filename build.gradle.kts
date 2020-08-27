
buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(Deps.android_gradle_plugin)
    classpath(Deps.kotlin_gradle_plugin)
    classpath(Deps.jetpack_nav_safeargs_plugin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}