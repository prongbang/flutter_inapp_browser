#import "FlutterInappBrowserPlugin.h"
#if __has_include(<flutter_inapp_browser/flutter_inapp_browser-Swift.h>)
#import <flutter_inapp_browser/flutter_inapp_browser-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_inapp_browser-Swift.h"
#endif

@implementation FlutterInappBrowserPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterInappBrowserPlugin registerWithRegistrar:registrar];
}
@end
