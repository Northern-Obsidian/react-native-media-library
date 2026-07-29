import type { TurboModule } from "react-native";
import { TurboModuleRegistry } from "react-native";

export interface Spec extends TurboModule {
  getAudio(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getVideos(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getImages(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getDocuments(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getAlbums(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getArtists(sort: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getGenres(sort: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getPlaylists(sort: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getFolders(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getFolderStatistics(folderPath: string | null): Promise<Record<string, any>[]>;
  getStatistics(): Promise<Record<string, any>>;
  search(options: Record<string, any>): Promise<Record<string, any>>;
  getById(mediaType: string, id: string): Promise<Record<string, any> | null>;
  getByUri(uri: string): Promise<Record<string, any> | null>;
  getRecent(mediaType: string | null, limit: number | null): Promise<Record<string, any>[]>;
  getFavorites(mediaType: string | null, sort: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>[]>;
  getLargestFiles(mediaType: string | null, limit: number | null): Promise<Record<string, any>[]>;
  getDuplicates(mediaType: string | null): Promise<Record<string, any>[]>;
  refresh(): Promise<void>;
  refreshIncremental(lastTimestamp: number | null): Promise<Record<string, any>>;
  getLastRefreshTimestamp(): Promise<number>;
  checkPermissions(): Promise<Record<string, any>>;
  requestPermissions(): Promise<Record<string, any>>;
  getAlbumArtwork(albumId: string): Promise<string | null>;
  getVideoThumbnail(videoId: string, width: number | null, height: number | null): Promise<string | null>;
  getImageThumbnail(imageId: string, width: number | null, height: number | null): Promise<string | null>;
  getLibrary(sort: Record<string, any> | null, filter: Record<string, any> | null, pagination: Record<string, any> | null): Promise<Record<string, any>>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>("MediaStore");
