Pod::Spec.new do |s|
  s.name           = "ExpoMediastore"
  s.version        = "1.0.0"
  s.summary        = "Universal high-performance media indexing library using MediaStore"
  s.homepage       = "https://github.com/Cadmus11/-cadmus11-expo-mediastore"
  s.license        = "MIT"
  s.author         = ""
  s.source         = { :git => "https://github.com/Cadmus11/-cadmus11-expo-mediastore.git" }
  s.static_framework = true

  s.platform       = :ios, "13.0"
  s.swift_version  = "5.4"
  s.source_files   = "Sources/**/*.{swift}"

  s.dependency "ExpoModulesCore"

  s.ios.deployment_target = "13.0"
end
