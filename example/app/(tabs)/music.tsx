import { useEffect, useState } from "react";
import { FlatList, Text, View, StyleSheet } from "react-native";
import { getAudio, getAlbums, AudioItem, Album } from "@obsidian_north/react-native-mediastore";

export default function MusicScreen() {
  const [songs, setSongs] = useState<AudioItem[]>([]);
  const [albums, setAlbums] = useState<Album[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      getAudio({ field: "artist", order: "asc" }),
      getAlbums(),
    ])
      .then(([s, a]) => {
        setSongs(s);
        setAlbums(a);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <View style={styles.center}>
        <Text>Loading music library...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.header}>{songs.length} songs across {albums.length} albums</Text>
      <FlatList
        data={songs}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.title}>{item.title}</Text>
            <Text style={styles.subtitle}>{item.artist} — {item.album}</Text>
            <Text style={styles.meta}>{(item.duration / 1000).toFixed(0)}s</Text>
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#fff" },
  center: { flex: 1, justifyContent: "center", alignItems: "center" },
  header: { padding: 16, fontSize: 16, fontWeight: "600", color: "#555" },
  row: { padding: 12, borderBottomWidth: 0.5, borderBottomColor: "#ddd" },
  title: { fontSize: 16, fontWeight: "500" },
  subtitle: { fontSize: 13, color: "#666", marginTop: 2 },
  meta: { fontSize: 11, color: "#999", marginTop: 2 },
});
