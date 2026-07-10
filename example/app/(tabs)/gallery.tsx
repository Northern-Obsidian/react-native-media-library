import { useEffect, useState } from "react";
import { FlatList, Image, Text, View, StyleSheet, Dimensions } from "react-native";
import { getImages, ImageItem } from "@obsidian_north/react-native-mediastore";

const NUM_COLUMNS = 3;
const SCREEN_WIDTH = Dimensions.get("window").width;
const ITEM_SIZE = SCREEN_WIDTH / NUM_COLUMNS;

export default function GalleryScreen() {
  const [images, setImages] = useState<ImageItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getImages({ field: "dateAdded", order: "desc" }, null, { limit: 100 })
      .then(setImages)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <View style={styles.center}>
        <Text>Loading gallery...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.header}>{images.length} photos</Text>
      <FlatList
        data={images}
        numColumns={NUM_COLUMNS}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.cell}>
            <Image source={{ uri: item.uri }} style={styles.image} resizeMode="cover" />
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#000" },
  center: { flex: 1, justifyContent: "center", alignItems: "center", backgroundColor: "#000" },
  header: { padding: 12, fontSize: 14, fontWeight: "600", color: "#fff" },
  cell: { width: ITEM_SIZE, height: ITEM_SIZE, padding: 1 },
  image: { flex: 1, borderRadius: 2 },
});
