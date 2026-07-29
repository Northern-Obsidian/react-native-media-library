module.exports = {
  dependency: {
    platforms: {
      android: {
        packageImportPath: "import com.obsidian_north.mediastore.MediaStorePackage;",
        packageInstance: "new MediaStorePackage()",
      },
      ios: {
        podspecPath: require.resolve("./ios/RNMediaStore.podspec"),
      },
    },
  },
};
