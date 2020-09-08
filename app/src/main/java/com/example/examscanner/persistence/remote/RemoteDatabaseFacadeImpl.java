package com.example.examscanner.persistence.remote;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;

class RemoteDatabaseFacadeImpl implements RemoteDatabaseFacade {

    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "RemoteDatabaseFacadeImpl";
    private final String DELETED = "DELETED";

    private void storeVersionBitmap(Bitmap verBm) {

    }

    @Override
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal, long sessionId, int numberOfQuestions, int uploaded,int numOfVersions) {
        return pushChildInPath(
                Paths.toExams,
                new Exam(
                        mangerId,
                        Arrays.asList(gradersIdentifiers),
                        courseName,
                        semester,
                        term,
                        year,
                        seal,
                        url,
                        numberOfQuestions,
                        uploaded,
                        numOfVersions
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<String> createVersion(int num, String remoteExamId, String pathToBitmap) {
        return pushChildInPath(
                Paths.toVersions,
                new Version(
                        remoteExamId,
                        num,
                        pathToBitmap
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<String> createQuestion(String remoteVersionId, int num, int ans, int left, int up, int right, int bottom) {
        return pushChildInPath(
                Paths.toQuestions,
                new Question(
                        num,
                        ans,
                        left,
                        up,
                        right,
                        remoteVersionId,
                        bottom
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public Observable<List<ExamineeSolution>> getExamineeSolutions() {
        return getChildrenOfRoot(
                Paths.toSolutions,
                ds -> {
                    if(!ds.child(ExamineeSolution.metaIsValid).getValue(Boolean.class)){
                        return ExamineeSolution.getInvalidInstance();
                    }
                    List<Long> value = (ArrayList<Long>) ds.child(ExamineeSolution.metaAnswers).getValue();
                    Map<Integer, Integer> answers = new HashMap<Integer, Integer>();
                    for (int i = 1; value!=null && i < value.size(); i++) {
                        answers.put(i, value.get(i).intValue());
                    }
                    ExamineeSolution examineeSolution = new ExamineeSolution(
                            ds.child(ExamineeSolution.metaVersionId).getValue(String.class),
                            answers,
                            ds.child(ExamineeSolution.metaIsValid).getValue(Boolean.class)
                    );
                    examineeSolution.examineeId = ds.child(ExamineeSolution.metaExamineeId).getValue(String.class);
                    examineeSolution._setId(ds.getKey());
                    return examineeSolution;
                }
        );
    }

    @Override
    public Observable<String> onlineInsertExamineeSolution(String examineeId, String versionId, boolean isValid) {
        return pushChildInPath(
                String.format("%s", Paths.toSolutions),
                new ExamineeSolution(
                        examineeId,
                        versionId,
                        0,
                        new HashMap<>(),
                        null,
                        null,
                        isValid
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }
    @Override
    public void setSolutionBitmapUrl(String url, String remoteId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, remoteId, ExamineeSolution.metaBitmapUrl),
                url,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void setOriginialBitmapUrl(String url, String remoteId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, remoteId, ExamineeSolution.metaOrigBitmapUrl),
                url,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void offilneInsertExamineeSolutionGrade(String remoteId, float grade) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, remoteId, ExamineeSolution.metaGrade),
                grade,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void offilneInsertExamineeSolutionGrader(String remoteId, String email) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, remoteId, ExamineeSolution.metaGrader),
                email,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void insertReserevedExamineeId(String remoteId, String reservedExamineeId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, remoteId, ExamineeSolution.metaExamineeId),
                reservedExamineeId,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void deleteVersion(String remoteVersionId) {
        putObjectInLocation(
                String.format("%s/%s", Paths.toVersions, remoteVersionId),
                null,
                StoreTaskPostprocessor.getOffline()
        );
    }

    @Override
    public void deleteQuestion(String remoteId) {
        putObjectInLocation(
                String.format("%s/%s", Paths.toQuestions, remoteId),
                null,
                StoreTaskPostprocessor.getOffline()
        );
    }

    @Override
    public Observable<String> offlineInsertExamineeSolutionTransaction(String examineeId, String versionId, int[][] answers, float grade, String bitmapUrl, String origBitmapUrl, boolean isValid) {
        HashMap ans = new HashMap();
        for (int i = 0; i < answers.length; i++)
            ans.put(String.valueOf(i + 1), answers[i][0]);
        return pushChildInPath(
                Paths.toSolutions,
                new ExamineeSolution(
                        examineeId,
                        versionId,
                        grade,
                        ans,
                        bitmapUrl,
                        origBitmapUrl,
                        isValid
                ),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public void offlineUpdateAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans) {
        offlineInsertAnswerIntoExamineeSolution(examineeId, questionNum, ExamineeSolution.mapAns(ans));
    }

    @Override
    public void offlineInsertAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans) {
        putObjectInLocation(
                String.format("%s/%s/%s/%d", Paths.toSolutions, examineeId, ExamineeSolution.metaAnswers, questionNum),
                new Integer(ans),
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void offlineInsertGradeIntoExamineeSolution(String examineeId, float grade) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions, examineeId, ExamineeSolution.metaGrade),
                new Float(grade),
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public void offlineDeleteExamineeSolution(String solutionId, String examineeId, String remoteExamId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.examineeIds, remoteExamId, cannonic(examineeId)),
                null,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
        putObjectInLocation(
                String.format("%s/%s", Paths.toSolutions, solutionId),
                null,
                StoreTaskPostprocessor.getOffline()
        ).subscribe();
    }

    @Override
    public Observable<String> addVersion(int num, String remoteExamId, String pathToBitmap) {
        return createVersion(num, remoteExamId, pathToBitmap);
    }

    @Override
    public Observable<List<Grader>> getGraders() {
        return getChildrenOfRoot(
                Paths.toGraders,
                ds -> {
                    return new Grader(
                            ds.child(Grader.metaEmail).getValue(String.class),
                            ds.child(Grader.metaUserId).getValue(String.class)
                    );
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Observable<List<Exam>> getExams() {
        return getChildrenOfRoot(
                Paths.toExams,
                new DataSnapshot2Obj<Exam>() {
                    DataSnapshotList2ObjList<String> graderListConverter = iterable -> {
                        List<String> ans = new ArrayList<>();
                        iterable.forEach(ds -> ans.add(ds.getValue().toString()));
                        return ans;
                    };

                    @Override
                    public Exam convert(DataSnapshot ds) {
                        try {
                            if(ds.getValue(String.class).equals(DELETED)){
                                return Exam.theDeletedExam(ds.getKey());
                            }
                        }catch (Exception e){

                        }
                        Exam exam = new Exam(
                                ds.child(Exam.metaManager).getValue(String.class),
                                graderListConverter.convert(ds.child(Exam.metaGraders).getChildren()),
                                ds.child(Exam.metaCourseName).getValue(String.class),
                                ds.child(Exam.metaSemester).getValue(Integer.class),
                                ds.child(Exam.metaTerm).getValue(Integer.class),
                                ds.child(Exam.metaYear).getValue(String.class),
                                ds.child(Exam.metaSeal).getValue(Boolean.class),
                                ds.child(Exam.metaUrl).getValue(String.class),
                                ds.child(Exam.metaqnum).getValue(Integer.class),
                                ds.child(Exam.metaUploaded).getValue(Integer.class),
                                ds.child(Exam.metaNumOfVersions).getValue(Integer.class)
                        );
                        exam._setId(ds.getKey());
                        return exam;
                    }
                }
        );
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Observable<List<Version>> getVersions() {
        return getChildrenOfRoot(
                Paths.toVersions,
                new DataSnapshot2Obj<Version>() {
                    @Override
                    public Version convert(DataSnapshot ds) {
                        Version version = new Version(
                                ds.child(Version.metaExamId).getValue(String.class),
                                ds.child(Version.metaVersionNumber).getValue(Integer.class),
                                ds.child(Version.metaBitmapPath).getValue(String.class)
                        );
                        version._getId(ds.getKey());
                        return version;
                    }
                }
        );
    }

    @Override
    public Observable<List<Question>> getQuestions() {
        return getChildrenOfRoot(
                Paths.toQuestions,
                new DataSnapshot2Obj<Question>() {
                    @Override
                    public Question convert(DataSnapshot ds) {
                        Question question = new Question(
                                ds.child(Question.metaNum).getValue(Integer.class),
                                ds.child(Question.metaAns).getValue(Integer.class),
                                ds.child(Question.metaLeft).getValue(Integer.class),
                                ds.child(Question.metaUp).getValue(Integer.class),
                                ds.child(Question.metaRight).getValue(Integer.class),
                                ds.child(Question.metaVersionId).getValue(String.class),
                                ds.child(Question.metaBottom).getValue(Integer.class)
                        );
                        question._setId(ds.getKey());
                        return question;
                    }
                }
        );
    }

    @NotNull
    protected <T> Observable<List<T>> getChildrenOfRoot(String pathToParent, DataSnapshot2Obj<T> ds2o) {
        return new Observable<List<T>>() {
            @Override
            protected void subscribeActual(Observer<? super List<T>> observer) {
                DatabaseReference ref = FirebaseDatabaseFactory.get().getReference(pathToParent);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> ans = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            ans.add(ds2o.convert(childSnapshot));
                        }
                        try {
                            observer.onNext(ans);
                            observer.onComplete();
                        }catch (NullPointerException e){
                            observer.onError(e);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        observer.onError(databaseError.toException());
                        observer.onComplete();
                    }
                });
            }
        };
    }


    public void addGraderIfAbsent(String email, String uId) {
        DatabaseReference ref = FirebaseDatabaseFactory.get().getReference(Paths.toGraders);
        ref.orderByChild("userId").equalTo(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //username exist
                } else {
                    createGrader(email, uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ESLogeerFactory.getInstance().log(TAG, "nom", databaseError.toException());
            }
        });
    }

    @Override
    public Observable<List<Exam>> getExamsOfGrader(String userId) {
        return getChildrenOfRoot(
                Paths.toExams,
                new DataSnapshot2Obj<Exam>() {
                    DataSnapshotList2ObjList<String> graderListConverter = iterable -> {
                        List<String> ans = new ArrayList<>();
                        iterable.forEach(ds -> ans.add(ds.getKey()));
                        return ans;
                    };

                    @Override
                    public Exam convert(DataSnapshot ds) {
                        Exam exam = new Exam(
                                ds.child(Exam.metaManager).getValue(String.class),
                                graderListConverter.convert(ds.child(Exam.metaGraders).getChildren()),
                                ds.child(Exam.metaCourseName).getValue(String.class),
                                ds.child(Exam.metaSemester).getValue(Integer.class),
                                ds.child(Exam.metaTerm).getValue(Integer.class),
                                ds.child(Exam.metaYear).getValue(String.class),
                                ds.child(Exam.metaSeal).getValue(Boolean.class),
                                ds.child(Exam.metaUrl).getValue(String.class),
                                ds.child(Exam.metaqnum).getValue(Integer.class),
                                ds.child(Exam.metaUploaded).getValue(Integer.class),
                                ds.child(Exam.metaNumOfVersions).getValue(Integer.class)
                        );
                        exam._setId(ds.getKey());
                        return exam;
                    }
                }
        );
    }


    @Override
    public Observable<String> createGrader(String email, String userId) {
        return pushChildInPath(
                String.format("%s", Paths.toGraders),
                new Grader(email, userId),
                StoreTaskPostprocessor.getOnline()
        );
    }

    @Override
    public void updateUploaded(String remoteId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toExams, remoteId , Exam.metaUploaded),
                new Integer(1),
                StoreTaskPostprocessor.getOffline()
        );
    }

    @Override
    public Observable<String> observeExamineeIds(String remoteId) {
        return observeList(
                String.format("%s/%s", Paths.examineeIds, remoteId),
                ds -> ds.getKey(),
                ds -> "REMOVE:"+ds.getKey()
        );
    }

    @Override
    public Observable<String> insertExamineeIDOrReturnNull(String remoteExamId, String examineeId) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(String.format("%s/%s/%s", Paths.examineeIds, remoteExamId, cannonic(examineeId)));
        return putObjectInLocation(
                String.format("%s/%s/%s", Paths.examineeIds, remoteExamId, cannonic(examineeId)),
                new Boolean(true),
                StoreTaskPostprocessor.getOnline()
        );
//        return new Observable<String>() {
//            @Override
//            protected void subscribeActual(Observer<? super String> observer) {
//                ref.runTransaction(new Transaction.Handler() {
//                    @NonNull
//                    @Override
//                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        if(mutableData.getValue()== null){
//                            mutableData.setValue(true);
//                            observer.onNext("COOLNESSNESS");
//                        }else{
//                            observer.onNext(null);
//                        }
//                        final Transaction.Result success = Transaction.success(mutableData);
//                        observer.onComplete();
//                        return success;
//                    }
//
//                    @Override
//                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//                    }
//                });
//            }
//        };
    }

    @Override
    public void offlineUpdateExamineeGrade(String remoteId, String examRemoteId, float grade) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions,remoteId, ExamineeSolution.metaGrade),
                new Float(grade),
                StoreTaskPostprocessor.getOffline()
        );

    }



    public void updateTotalAndAverageTransaction(String examRemoteId, Float grade) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(String.format("%s/%s", Paths.toExams, examRemoteId));
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                 Exam exam = mutableData.getValue(Exam.class);
                if (exam == null) {
                    // shouldn't get here
                    return Transaction.success(mutableData);
                }
                // Set value and report transaction success
                int prevTotal = exam.total;
                exam.total = prevTotal + 1;
                exam.average = (exam.average * prevTotal + grade) / exam.total;
                mutableData.setValue(exam);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
                Log.d(TAG, "incTotalTransaction:onComplete:" + databaseError);
            }
        });
    }

    private void updateTotalTransaction(String examRemoteId, float grade) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(String.format("%s/%s/%s", Paths.toExams, examRemoteId, Exam.metaTotal));
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer prevTotal = mutableData.getValue(Integer.class);
                if (prevTotal == null) {
                    return Transaction.success(mutableData);
                }
                // Set value and report transaction success
                mutableData.setValue(prevTotal+1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
                Log.d(TAG, "incTotalTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public void validateSolution(String remoteId) {
        putObjectInLocation(
                String.format("%s/%s/%s", Paths.toSolutions,remoteId, ExamineeSolution.metaIsValid),
                new Boolean(true),
                StoreTaskPostprocessor.getOffline()
        );
    }

    @Override
    public void deleteExam(String examId) {
        putObjectInLocation(
                String.format("%s/%s", Paths.toExams,examId),
                DELETED,
                StoreTaskPostprocessor.getOffline()
        );
    }

    private DataSnapshot debugPrintDB(){
        Observable<DataSnapshot>  o = new Observable<DataSnapshot>() {
            @Override
            protected void subscribeActual(Observer<? super DataSnapshot> observer) {
                DatabaseReference ref = FirebaseDatabaseFactory.get().getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            observer.onNext(dataSnapshot);
                            observer.onComplete();
                        }catch (NullPointerException e){
                            observer.onError(e);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        observer.onError(databaseError.toException());
                        observer.onComplete();
                    }
                });
            }
        };
        return o.blockingFirst();
    }

    private String cannonic(String examineeId) {
        return examineeId.replace('\\','_').replace('/', '_');
    }

    private <T> Observable<T> observeList(String path, DSMap<T> map,DSMap<T> removeMap) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        return generateChildrenObserver(reference, map,removeMap);
    }

    private <T> Observable<T> generateChildrenObserver(DatabaseReference reference, DSMap<T> map, DSMap<T> removeMap) {
        return new Observable<T>() {
            @Override
            protected void subscribeActual(Observer<? super T> observer) {
                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        observer.onNext(map.map(dataSnapshot));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        observer.onNext(map.map(dataSnapshot));
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        observer.onNext(removeMap.map(dataSnapshot));
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey() + " ExamScanner doestn handle!");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey() + " ExamScanner doestn handle!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        observer.onError(databaseError.toException());
                    }
                };
                reference.addChildEventListener(childEventListener);
            }
        };
    }

    private static Observable<String> pushChildInPath(String parent, Object obj, StoreTaskPostprocessor taskPostprocessor) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(parent).push(), obj, taskPostprocessor);
    }

    private Observable<String> putObjectInLocation(String location, Object obj, StoreTaskPostprocessor taskPostprocessor) {
        return storeObject(() -> FirebaseDatabaseFactory.get().getReference(location), obj, taskPostprocessor);
    }

    private static Observable<String> storeObject(ReferenceLocationAllocator allocator, Object obj, StoreTaskPostprocessor postProcessor) {
        DatabaseReference myRef = allocator.allocate();
        final Task<Void> task = myRef.setValue(obj);
        return postProcessor.postProcess(task, myRef);
//        return Observable.fromCallable(() -> {
//            Tasks.await(task);
//            if (task.isSuccessful()) {
//                return myRef.getKey();
//            } else {
//                throw new IllegalStateException("Task not successful", task.getException());
//            }
//        });
    }


    private interface ReferenceLocationAllocator {
        DatabaseReference allocate();
    }

    private interface DataSnapshot2Obj<T> {
        T convert(DataSnapshot ds);
    }

    private interface DataSnapshotList2ObjList<T> {
        List<T> convert(Iterable<DataSnapshot> ds);
    }

    private interface StoreTaskPostprocessor {
        Observable<String> postProcess(Task t, DatabaseReference ref);
        static StoreTaskPostprocessor getOnline() {
            return new StoreTaskPostprocessor() {
                @Override
                public Observable<String> postProcess(Task t, DatabaseReference ref) {
                    return Observable.fromCallable(() -> {
                        Tasks.await(t);
                        if (t.isSuccessful()) {
                            return ref.getKey();
                        } else {
                            throw new IllegalStateException("Task not successful", t.getException());
                        }
                    });
                }
            };
        }

        static StoreTaskPostprocessor getOffline() {
            return new StoreTaskPostprocessor() {
                private static final String DEBUG_TAG = "DebugExamScanner";

                @Override
                public Observable<String> postProcess(Task t, DatabaseReference ref) {
                    return Observable.fromCallable(() -> {
                        t.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(DEBUG_TAG, MSG_PREF + "::offlineStoreObject SUCCESS");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(DEBUG_TAG, MSG_PREF + "::offlineStoreObject FAILURE", e);
                            }
                        });
                        return "return";
                    });
                }
            };
        }
    }

    interface DSMap<T> {
        T map(DataSnapshot ds);
    }
}
