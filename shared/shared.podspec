Pod::Spec.new do |spec|
    spec.name                     = 'shared'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://github.com/toaderandrei/popular-movies-kt'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Andrei Toader-Stanescu'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Popular Movies KMP Shared Module'
    spec.vendored_frameworks      = 'build/outputs/framework/shared.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '14.0'

    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':shared',
        'PRODUCT_MODULE_NAME' => 'shared',
    }

    spec.script_phases = [
        {
            :name => 'Build shared',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to 'YES'"
                  exit 0
                fi
                set -e
                REPO_ROOT="$PODS_TARGET_SRCROOT/.."
                "$REPO_ROOT/gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:embedAndSignAppleFrameworkForXcode
            SCRIPT
        }
    ]

end