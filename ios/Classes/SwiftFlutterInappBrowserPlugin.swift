import Flutter
import UIKit

public class SwiftFlutterInappBrowserPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_inapp_browser", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterInappBrowserPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
