Pod::Spec.new do |spec|
    spec.name                     = 'shared_ui'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://github.com/toaderandrei/popular-movies-kt'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Andrei Toader-Stanescu'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Popular Movies KMP Shared UI Module'
    spec.vendored_frameworks      = 'build/outputs/framework/shared_ui.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '14.0'
    spec.dependency                 'shared'

    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':shared-ui',
        'PRODUCT_MODULE_NAME' => 'shared_ui',
    }

    spec.script_phases = [
        {
            :name => 'Build shared_ui',
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