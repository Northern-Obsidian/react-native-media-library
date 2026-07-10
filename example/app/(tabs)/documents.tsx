import { useEffect, useState } from "react";
import { FlatList, Text, View, StyleSheet } from "react-native";
import { getDocuments, getFolders, DocumentItem, Folder } from "@cadmuslabs/react-native-mediastore";

export default function DocumentsScreen() {
  const [files, setFiles] = useState<DocumentItem[]>([]);
  const [folders, setFolders] = useState<Folder[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      getDocuments({ field: "dateModified", order: "desc" }),
      getFolders(),
    ])
      .then(([f, d]) => {
        setFiles(f);
        setFolders(d);
      })
      .finally(() => setLoading(false));
  }, []);

  const formatSize = (bytes: number) => {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  };

  if (loading) {
    return <View style={styles.center}><Text>Loading documents...</Text></View>;
  }

  return (
    <View style={styles.container}>
      <Text style={styles.header}>{folders.length} folders, {files.length} files</Text>
      <FlatList
        ListHeaderComponent={
          folders.length > 0 ? (
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>Folders</Text>
              {folders.map((f) => (
                <View key={f.id} style={styles.row}>
                  <Text style={styles.title}>{f.name}</Text>
                  <Text style={styles.meta}>{f.fileCount} files · {formatSize(f.totalSize)}</Text>
                </View>
              ))}
            </View>
          ) : null
        }
        data={files}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <Text style={styles.title}>{item.name}</Text>
            <Text style={styles.meta}>{item.extension?.toUpperCase()} · {formatSize(item.size)}</Text>
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#fff" },
  center: { flex: 1, justifyContent: "center", alignItems: "center" },
  header: { padding: 16, fontSize: 14, fontWeight: "600", color: "#555" },
  section: { paddingHorizontal: 16, paddingBottom: 8 },
  sectionTitle: { fontSize: 13, fontWeight: "700", color: "#888", textTransform: "uppercase", marginBottom: 8 },
  row: { padding: 12, borderBottomWidth: 0.5, borderBottomColor: "#eee" },
  title: { fontSize: 15, fontWeight: "500" },
  meta: { fontSize: 12, color: "#888", marginTop: 2 },
});
