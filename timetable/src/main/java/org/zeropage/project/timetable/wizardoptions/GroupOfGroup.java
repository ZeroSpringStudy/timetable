package org.zeropage.project.timetable.wizardoptions;

import lombok.Data;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupOfGroup {
    private String name;
    private List<LectureGroup> lectureGroups = new ArrayList<>();
    private int numOfGroups;
    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * 의미없는 결과를 줄이기 위해 마지막 우선순위를 한번에 시간표에 표시할 때 사용.
     * Low priority. 후순위 구현 대상.
     */
    private boolean isLastPriority;

    public GroupOfGroup() {
        numOfGroups = 1;
        isLastPriority = false;
    }

    public void addGroup(LectureGroup group){
        lectureGroups.add(group);
    }

    public void addGroup(){
        lectureGroups.add(new LectureGroup());
    }

    public void deleteGroup(LectureGroup group){
        lectureGroups.remove(group);
    }

    void calculateResult(Wizard options,
                         ArrayList<Timetable> resultList,
                         ArrayList<LectureGroup> selectedGroups,
                         ArrayList<GroupOfGroup> remainingGroups) {
        calculateResult(options,0,numOfGroups,resultList,
                selectedGroups,remainingGroups);
    }

    private void calculateResult(Wizard options, int startIndex, int numOfLeftGroup,
                         ArrayList<Timetable> resultList,
                         ArrayList<LectureGroup> selectedGroups,
                         ArrayList<GroupOfGroup> remainingGroups) {
        for (int i = startIndex; i <= lectureGroups.size() - numOfLeftGroup; i++) {
            ArrayList<LectureGroup> newlySelectedGroups = new ArrayList<>(selectedGroups);
            newlySelectedGroups.add(lectureGroups.get(i));
            if (numOfLeftGroup != 1) {
                //이 GroupOfGroup에서 LectureGroup를 더 골라야 함
                //예를 들어 핵교 5개 영역 중 2개를 고르도록 했다면, 하나를 방금 골랐고 나머지 하나를 골라야 함
                calculateResult(options, i + 1, numOfLeftGroup - 1,
                        resultList, newlySelectedGroups, remainingGroups);
            } else {
                //이 group에서 LectureGroup 생성이 끝남(방금 고른 게 마지막)
                if (remainingGroups.size() != 0) {
                    //다음 group이 있어 넘겨 주어야 함
                    ArrayList<GroupOfGroup> nextRemainingGroups = new ArrayList<>(remainingGroups);
                    GroupOfGroup nextGroup = nextRemainingGroups.remove(0);
                    nextGroup.calculateResult(options, resultList,
                            newlySelectedGroups, nextRemainingGroups);
                } else {
                    //모든 LectureGroup 생성이 끝남
                    ArrayList<LectureGroup> finallySelectedGroups =
                            new ArrayList<>(newlySelectedGroups);
                    LectureGroup firstGroup = finallySelectedGroups.remove(0);
                    firstGroup.calculateResult(options, resultList,
                            new ArrayList<Lecture>(),
                            finallySelectedGroups);
                }
            }
        }
    }
}