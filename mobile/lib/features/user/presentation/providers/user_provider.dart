import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/datasources/user_remote_datasource.dart';
import '../../data/models/user_profile_model.dart';
import '../../../../core/network/api_client.dart';

final userDataSourceProvider = Provider<UserRemoteDataSource>(
  (ref) => UserRemoteDataSource(ApiClient.instance),
);

final userProfileProvider = AsyncNotifierProvider.autoDispose
    .family<UserProfileNotifier, UserProfileModel, String>(
  UserProfileNotifier.new,
);

class UserProfileNotifier
    extends AutoDisposeFamilyAsyncNotifier<UserProfileModel, String> {
  @override
  Future<UserProfileModel> build(String arg) async {
    final ds = ref.read(userDataSourceProvider);
    return ds.getProfile(arg);
  }

  Future<void> updateProfile({
    String? displayName,
    String? bio,
    String? city,
    String? suburb,
  }) async {
    final ds = ref.read(userDataSourceProvider);
    final updated = await ds.updateProfile(
      displayName: displayName,
      bio: bio,
      city: city,
      suburb: suburb,
    );
    state = AsyncData(updated);
  }

  Future<void> uploadAvatar(String filePath) async {
    final ds = ref.read(userDataSourceProvider);
    final updated = await ds.uploadAvatar(filePath);
    state = AsyncData(updated);
  }
}
