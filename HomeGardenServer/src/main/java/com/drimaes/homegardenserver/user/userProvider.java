package com.drimaes.homegardenserver.user;

import com.drimaes.homegardenserver.config.BaseException;
import com.drimaes.homegardenserver.config.secret.Secret;
import com.drimaes.homegardenserver.user.model.*;
import com.drimaes.homegardenserver.utils.AES128;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.drimaes.homegardenserver.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
// [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class userProvider {


    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private userDAO userDao;
    ////private JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public userProvider(userDAO userDao)
    {
        this.userDao = userDao;
    }
    /*@Autowired //readme 참고
    public userProvider(userDAO userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }*/
    // ******************************************************************************


    // 로그인(password 검사)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 암호화
            // 회원가입할 때 비밀번호가 암호화되어 저장되었기 떄문에 로그인을 할때도 암호화된 값끼리 비교를 해야합니다.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getClientPW().equals(password)) { //비말번호가 일치한다면 userIdx를 가져온다.
            return new PostLoginRes(userDao.getPwd(postLoginReq).getHomeGarden_barcode(), userDao.getPwd(postLoginReq).getStatus());

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // 해당 이메일이 이미 User Table에 존재하는지 확인
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // User들의 정보를 조회
    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 nickname을 갖는 User들의 정보 조회
    public List<GetUserRes> getUsersByNickname(String nickname) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByNickname(nickname);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 userIdx를 갖는 User의 정보 조회
    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //Test용 회원 홈가든 식물 닉네임 가져오기 메서드
    public GetUserPlantNickNameRes getUserPlantNickName(GetUserReq getUserReq) throws BaseException{
        try{
            GetUserPlantNickNameRes getUserPlantNickNameRes = userDao.getClienPlantNickName(getUserReq);
            return getUserPlantNickNameRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //사용자 ID중복 확인 메서드
    public String isDuplicatedUser(GetIsDuplicatedUserReq getIsDuplicatedUserReq) throws BaseException{
        try{
            int result = userDao.getIsDuplicatedUser(getIsDuplicatedUserReq);
            if(result == 0 ) return "true";
            else return "false";

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현재 식물 상태 반환
    public GetPlantStatusRes getPresentPlantStatus(GetPresentPlantStatusReq getPresentPlantStatusReq) throws BaseException{
        try{
            GetPlantStatusRes getPlantStatusRes = userDao.getPresentPlantStatus(getPresentPlantStatusReq);
            return getPlantStatusRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //과거 식물 상태 반환
    public List<GetPlantStatusRes> getHistoryPlantStatus(GetHistoryPlantStatusReq getHistoryPlantStatusReq) throws BaseException{
        try{
            List<GetPlantStatusRes> getPlantStatusRes= userDao.getHistoryPlantStatus(getHistoryPlantStatusReq);
            return getPlantStatusRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현재 식물 모드 반환
    public PostUserModeRes postPlantMode(GetUserReq getUserReq) throws BaseException{
        try{
            PostUserModeRes postUserModeRes = userDao.postPlantMode(getUserReq);
            return postUserModeRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
