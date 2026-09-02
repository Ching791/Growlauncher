Packaged Growtopia 5.54 game data.

The engine loads its data by opening this APK as a zip and reading paths under "assets/",
exactly like the official APK does, so the layout here must mirror the official one
(interface/, audio/, fonts/, game/, items.dat, cacert.pem, ...) with no extra top level
directory. Nesting everything under assets/growtopia/ makes every asset lookup fail.
