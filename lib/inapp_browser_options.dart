import 'package:flutter_inapp_browser/inapp_browser_transition.dart';

class InappBrowserOptions {
  String transition;
  String toolbarColor;

  InappBrowserOptions({
    this.transition = InappBrowserTransition.toTop,
    this.toolbarColor = '#ffffff',
  });
}
