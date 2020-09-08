package com.example.examscanner.persistence.remote.files_management;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.file.Paths;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;

class RemoteFilesManagerImpl implements RemoteFilesManager {
    FirebaseStorage storage;
    private boolean inTestMode = false;

    public RemoteFilesManagerImpl() {
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public void tearDown() {
        if (!inTestMode) {
            throw new RuntimeException("Someone tires to teardown the db while not in test mode");
        }
        tearDownDir(storage.getReference("test"));
    }

    private void tearDownDir(StorageReference reference) {
        reference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            tearDownDir(prefix);
                            deleteRef(prefix);
                        }

                        for (StorageReference item : listResult.getItems()) {
                            deleteRef(item);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    private void deleteRef(StorageReference prefix) {
        // Delete the file
        prefix.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    @Override
    public void setTestMode() {
        inTestMode = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String toPathString(String path) {
        String ans = path;
        if (inTestMode) {
            ans = "test/" + path;
        }
        return ans;
    }

    @Override
    public Observable<String> createUrl(String pathToRemoteBm) {
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                storage.getReference(toPathString(pathToRemoteBm)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        observer.onNext(uri.toString());
                        observer.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        observer.onError(e);
                        observer.onComplete();
                    }
                });
            }
        };
    }

    @Override
    public void deleteExam(String remoteId) {
        tearDownDir(storage.getReference(remoteId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Completable store(String path, byte[] data) {
        StorageReference ref = storage.getReference(toPathString(path));
        return Completable.fromAction(() -> {
            UploadTask uploadTask = ref.putBytes(data);
            Task t = uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Completable.error(exception);
                    Completable.complete();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Completable.complete();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
            Tasks.await(t);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Observable<byte[]> get(String path) {
        return new Observable<byte[]>() {
            @Override
            protected void subscribeActual(Observer<? super byte[]> observer) {
                StorageReference ref = storage.getReference(toPathString(path));
                final long ONE_MEGABYTE = 1024 * 1024 * 5;
                ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        observer.onNext(bytes);
                        observer.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        observer.onError(exception);
                    }
                });
            }
        };
    }

}
