import 'package:flutter/material.dart';
import 'package:flutter_inapp_browser/flutter_inapp_browser.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
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
