import { useState, useCallback } from "react";
import { FlatList, Text, TextInput, View, StyleSheet } from "react-native";
import { search, SearchResult } from "@cadmus11/mediastore";

export default function SearchScreen() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<SearchResult | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = useCallback(async (text: string) => {
    setQuery(text);
    if (text.length < 2) {
      setResults(null);
      return;
    }
    setLoading(true);
    try {
      const res = await search({
        query: text,
        types: ["audio", "video", "image", "document"],
        pagination: { limit: 50 },
      });
      setResults(res);
    } finally {
      setLoading(false);
    }
  }, []);

  const items = results
    ? [
        ...results.audio.map((a) => ({ id: a.id, label: a.title, sub: a.artist, type: "🎵" })),
        ...results.videos.map((v) => ({ id: v.id, label: v.title, sub: `${v.duration}s`, type: "🎬" })),
        ...results.images.map((i) => ({ id: i.id, label: i.title ?? "Untitled", sub: `${i.width}x${i.height}`, type: "🖼" })),
        ...results.documents.map((d) => ({ id: d.id, label: d.name, sub: d.extension?.toUpperCase() ?? "", type: "📄" })),
      ]
    : [];

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        placeholder="Search media..."
        placeholderTextColor="#999"
        value={query}
        onChangeText={handleSearch}
        autoCapitalize="none"
        autoCorrect={false}
      />
      {loading && (
        <View style={styles.center}>
          <Text>Searching...</Text>
        </View>
      )}
      {results && !loading && (
        <Text style={styles.count}>{results.totalCount} results</Text>
      )}
      <FlatList
        data={items}
        keyExtractor={(item) => item.id + item.type}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.icon}>{item.type}</Text>
            <View style={styles.info}>
              <Text style={styles.title}>{item.label}</Text>
              <Text style={styles.subtitle}>{item.sub}</Text>
            </View>
          </View>
        )}
        ListEmptyComponent={
          query.length >= 2 && !loading ? (
            <View style={styles.center}>
              <Text style={{ color: "#888" }}>No results</Text>
            </View>
          ) : null
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#fff" },
  center: { padding: 40, alignItems: "center" },
  input: {
    margin: 16,
    padding: 12,
    borderRadius: 10,
    backgroundColor: "#f0f0f0",
    fontSize: 16,
  },
  count: { paddingHorizontal: 16, fontSize: 13, color: "#888", marginBottom: 8 },
  row: { flexDirection: "row", alignItems: "center", padding: 12, borderBottomWidth: 0.5, borderBottomColor: "#eee" },
  icon: { fontSize: 22, marginRight: 12 },
  info: { flex: 1 },
  title: { fontSize: 15, fontWeight: "500" },
  subtitle: { fontSize: 12, color: "#888", marginTop: 2 },
});
