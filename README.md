# KakaoImages
[![Platform](https://img.shields.io/badge/platform-Android-green.svg) ]()
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![Language](https://img.shields.io/github/languages/top/Nexters/Yetda_Android)]()

[카카오 이미지 검색 API](https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-image)를 이용하여 필터링이 가능한 결과리스트를 보여준다.


# Requirements
- 로딩화면, 검색화면으로 구성된다.
- 검색이 용이하며 결과를 바로 확인할 수 있다.
- 검색된 결과는 하단의 리스트에 표시된다
- thumbnail_url를 반드시 포함한다.
- 검색된 리스트의 collection을 spinner에 표시하며 기본으로 ALL이 존재한다. 
- spinner 변경시 해당 collection만 보이도록 필터링한다.
- API KEY 및 BASE_URL은 별도의 api.properties에서 관리한다.
  
*새로고침(SwipeRefresh or Button)은 입력값에 따라 이미지가 실시간으로 변경되기 때문에 기능에서 제한다*

# How to
- 로딩화면, 검색화면 구성된다.
   ```
   1. 로딩화면은 테마를 사용하여 구현한다.
   2. 검색화면은 Activity로 구현한다.
   ```
- 검색이 용이하며 결과를 바로 확인할 수 있다.
  ```
  1. [검색]버튼 대신 입력값에 따라 실시간으로 검색한다.
  2. 바로 검색결과가 출력되기 때문에 스크롤 시 키보드를 숨긴다.
  3. 입력된 값이 있으면 우측의 x버튼을 통해 초기화 시킬 수 있다.
  4. 검색결과가 없을 시 "Result is Empty" 메세지를 표시한다.
  ```
- 검색된 결과는 하단의 리스트에 표시된다
  ```
  1. 열 2개씩 GridView로 보여준다.
  2. 50개마다 페이징 처리를 한다.
    --paging 을 사용할 때 추가된&필터링된 리스트를 PagedList 처리에 실패--
  3. 스크롤이 최하단에 도착했을 경우 API를 호출한다.
  ```
- spinner 변경시 해당 collection만 보이도록 필터링한다.
  ```
  1. RecyclerView의 Filterable을 이용한다.
  2. 페이징된 데이터는 별도의 filter를 생성하여 처리한다.
  ```
  

# Project Management
## Commit Message
- [템플릿](git-commit-template.txt)을 사용하여 관리한다.
- 제목에 라벨을 표시한다.
- 제목과 본문을 빈 줄로 구분한다.
- 제목의 길이는 50자로 제한한다.
- 제목에 .(마침표)를 사용하지 않는다.
- 설명에 어떻게 보다는 무엇을, 왜 변경했는지 설명한다.
- 설명에 여러 줄의 메시지를 작성할 땐 "-"로 구분한다.
- 하단에 관련된 이슈넘버를 첨부한다.
```
라벨 목록
- feature   : 새로운 기능
- bug       : 버그 수정
- refactor  : 리팩토링
- docs      : 문서(추가, 수정, 삭제)
- test      : 테스트
- etc       : 기타 변경 사항
```

## Tech Stack & References
- MVVM Architecture
- Jetpack ViewModel
- [Coroutine](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) - 비동기 작업
- [Koin](https://github.com/InsertKoinIO/koin) - 의존성 주입
- [Retrofit2 & Gson & Converter](https://github.com/square/retrofit) - REST API  통신
- [OkHttp3](https://github.com/square/okhttp) - Retrofit 및 로깅
- [Glide](https://github.com/bumptech/glide) - 이미지 로딩
- [LeakCanary](https://square.github.io/leakcanary/) - 디버깅시 메모리 누수 확인
- Ripple 애니메이션 적용
- [EndlessRecyclerViewScrollListener](https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView) - 최하단 스크롤 체크
- [ClearEditTextListener](https://stackoverflow.com/a/48146524) - EditText에 값 입력시 clear 버튼 생성 및 제어
