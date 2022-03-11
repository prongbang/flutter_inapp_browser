import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_inapp_browser/inapp_browser_options.dart';
import 'package:flutter_inapp_browser/inapp_browser_transition.dart';

export 'inapp_browser_options.dart';
export 'inapp_browser_transition.dart';

class FlutterInappBrowser {
  static const MethodChannel _channel = MethodChannel('flutter_inapp_browser');

  static Future<bool> launch(String url, {InappBrowserOptions? options}) async {
    return await _channel.invokeMethod('launch', {
      'url': url,
      'transition': options?.transition ?? InappBrowserTransition.toTop,
      'toolbarColor': options?.toolbarColor ?? '#ffffff',
    });
  }
}
