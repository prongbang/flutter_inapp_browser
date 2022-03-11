# flutter_inapp_browser

A Flutter plugin for launching a URL using Custom Tabs.

## Getting Started

It is really easy to use!
You should ensure that you add the `flutter_inapp_browser` as a dependency in your flutter project, Supported Android only.

```yaml
dependencies:
  flutter_inapp_browser:
    git: https://github.com/prongbang/flutter_inapp_browser.git
```

## Usage

```dart
import 'package:flutter/material.dart';
import 'package:flutter_inapp_browser/flutter_inapp_browser.dart';

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Plugin example app')),
        body: Center(
          child: TextButton(
            onPressed: () async {
              final success = await FlutterInappBrowser.launch(
                'https://google.com',
                options: InappBrowserOptions(
                  transition: InappBrowserTransition.toTop,
                  toolbarColor: '#ffffff',
                ),
              );
              if (!success) {
                print('Cannot open custom tabs');
              }
            },
            child: const Text('Open'),
          ),
        ),
      ),
    );
  }
}
```