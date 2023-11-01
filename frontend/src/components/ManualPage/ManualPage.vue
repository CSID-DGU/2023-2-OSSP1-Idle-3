<template>
  <v-sheet width="100%">
    <v-sheet
      max-width="500"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 0;
        z-index: 2;
      "
    >
      <nav-header></nav-header>
      <v-tabs
        v-model="tab"
        background-color="transparent"
        color="var(--main-col-1)"
        height="40"
        grow
      >
        <v-tab v-for="item in items" :key="item">
          <span class="sm-font medium-font">{{ item }}</span>
        </v-tab>
      </v-tabs>
    </v-sheet>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-sheet style="padding: 110px 25px 65px 25px">
          <div class="mb-5 d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Almost There?</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xs-font light-font">
              <span>Almost There은 모임 관리를 </span>
              <span class="orange-line">편하고, 재밌게</span>
              <span> 할 수 있는 서비스입니다.</span>
              <br />
              <span>
                최근 모임에서 지각하거나, 모임 장소와 시간에 대해 여러 번 묻는
                친구가 있어 불편함을 겪은 적이 있으신가요?
              </span>
              <br />
              <span> 저희는 이러한 분들을 위해 </span>
              <span class="yellow-line">Almost There</span>
              <span>를 기획했습니다. </span>
              <span class="green-line">모임을 잡는 순간부터</span>
              &nbsp;
              <span class="blue-line">모임이 끝난 후 정산까지</span>
              <span> Almost There에서 모임을 편리하게 관리해 보세요!</span>
            </div>
          </div>
          <div class="mb-5 d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Open Source</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xs-font light-font main-col-1">
              <span>Almost There 프로젝트는 </span>
              <span class="orange-line">오픈소스 프로젝트</span>
              <span>로 진행됐습니다.</span>
              <span>
                해당 프로젝트에 기여하고 싶으시다면 아래 주소에 접속해 코드를
                확인하고
              </span>
              <span>
                Fork, branch 생성 및 개발, merge request 요청 순으로 진행하시면
                됩니다.
              </span>
              <br />
              <a href="https://lab.ssafy.com/s08-final/S08P31A401"
                >S08P31A401 Repository</a
              >
              <span>에서 오픈소스 관련 문서를 확인해 주세요.</span>
            </div>
          </div>
          <div class="d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Developer</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xxs-font d-flex flex-column light-font">
              <div>
                <span class="mr-1 extrabold-font">진행</span>
                <span>삼성청년SW아카데미, SSAFY 8기 자율 프로젝트</span>
              </div>
              <div>
                <span class="mr-1 extrabold-font">팀명</span>
                <span>A401 어디야?</span>
              </div>
              <div>
                <span class="mr-1 extrabold-font">팀장</span>
                <span>엄희정</span>
                <span class="mx-1 extrabold-font">발표자</span>
                <span>지강훈</span>
              </div>
              <div>
                <span class="mr-1 extrabold-font">문의</span>
                <span>hihejyop@gmail.com</span>
              </div>
            </div>
            <div class="sm-font">
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  실시간 위치 공유
                </v-chip>
                <span class="light-font xs-font">엄희정 지강훈</span>
              </div>
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  중간 위치 찾기
                </v-chip>
                <span class="light-font xs-font">안승태 이종민</span>
              </div>
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  정산 및 실시간 채팅
                </v-chip>
                <span class="light-font xs-font">신도연 서지윤</span>
              </div>
            </div>
          </div>
        </v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 110px 25px 65px 25px">
          <div
            class="mb-2 d-flex flex-column point-font main-col-1 xxxxl-font semi2narrow-font"
          >
            <span>모임을 만들고 관리하고</span>
            <span>다양한 기능을 이용해보세요!</span>
            <v-divider
              class="mt-2"
              width="300"
              style="border-color: var(--main-col-1)"
            ></v-divider>
          </div>
          <div class="d-flex flex-column flex-no-strech">
            <div class="d-flex flex-column mt-3">
              <v-chip color="var(--red-col)" dark>
                <span class="mr-2 black-font">STEP 1</span>
                <span>모임 등록하기</span>
              </v-chip>
              <div class="pa-2 light-font xs-font main-col-1">
                <span>홈페이지 하단 좌측에</span>
                <v-icon class="mx-1" style="padding-bottom: 2px" small>
                  $vuetify.icons.register_outline
                </v-icon>
                <span>버튼을 클릭하면 </span>
                <span>등록 페이지로 이동합니다.</span>
                <br />
                <v-sheet elevation="2" class="mx-2 px-4 mt-4 py-4" rounded>
                  <!-- 제목 -->
                  <v-sheet
                    width="100%"
                    class="d-flex flex-row align-center"
                    elevation="1"
                    height="30"
                    rounded
                  >
                    <v-sheet
                      width="50"
                      height="100%"
                      color="var(--red-col)"
                      class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font sm-font main-col-1"
                    >
                      <span>제목</span>
                    </v-sheet>
                    <div class="px-2 main-col-2 xxxs-font">
                      제목을 입력해 주세요.
                    </div>
                  </v-sheet>

                  <!-- 날짜 -->
                  <v-sheet
                    width="100%"
                    class="mt-2 d-flex flex-row align-center"
                    elevation="1"
                    rounded
                    height="30"
                  >
                    <v-sheet
                      height="100%"
                      width="50"
                      color="var(--yellow-col)"
                      class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font sm-font main-col-1"
                    >
                      <span>날짜</span>
                    </v-sheet>
                    <div class="px-2 main-col-2 xxxs-font">
                      날짜를 선택해 주세요.
                    </div>
                  </v-sheet>

                  <!-- 시간 -->
                  <v-sheet
                    width="100%"
                    class="mt-2 d-flex flex-row align-center"
                    elevation="1"
                    rounded
                    height="30"
                  >
                    <v-sheet
                      height="100%"
                      width="50"
                      color="var(--green-col)"
                      class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font sm-font main-col-1"
                    >
                      시간
                    </v-sheet>
                    <div class="px-2 main-col-2 xxxs-font">
                      시간을 선택해 주세요.
                    </div>
                  </v-sheet>

                  <!-- 장소 -->
                  <v-sheet
                    width="100%"
                    class="mt-2 d-flex flex-row align-center"
                    elevation="1"
                    rounded
                    height="30"
                  >
                    <v-sheet
                      height="100%"
                      width="50"
                      color="var(--blue-col)"
                      class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font sm-font main-col-1"
                    >
                      장소
                    </v-sheet>
                    <div class="px-2 main-col-2 xxxs-font">
                      장소를 선택해 주세요.
                    </div>
                  </v-sheet>
                  <v-btn
                    elevation="3"
                    color="var(--main-col-1)"
                    dark
                    block
                    small
                    class="mt-2"
                  >
                    모임 등록하기
                  </v-btn>
                </v-sheet>
                <br />
                <span>등록 페이지에서 제목, 날짜, 시간, 장소를 입력하고</span>
                <v-btn
                  class="mx-1"
                  color="var(--main-col-1)"
                  elevation="0"
                  x-small
                  dark
                  >모임 등록하기</v-btn
                >
                <span>버튼을 클릭해 주세요. </span>
                <span
                  >시간은 현재 시간 이후로 설정해야 하며, 모든 정보를 입력해야
                  등록이 가능합니다.</span
                >
              </div>
            </div>
            <div class="d-flex flex-column mt-3">
              <v-chip color="var(--yellow-col)" dark>
                <span class="mr-2 black-font">STEP 2</span>
                <span>친구 초대하기</span>
              </v-chip>
              <div class="pa-2 light-font xs-font main-col-1">
                <span>
                  모임 등록을 완료했으면 초대 링크를 통해 모임에 친구를 초대할
                  수 있습니다.
                </span>
                <br />
                <span>홈페이지 하단 우측의 </span>
                <span class="orange-line">마이페이지</span>
                <v-icon class="mx-1" style="padding-bottom: 2px" small>
                  $vuetify.icons.mypage_outline
                </v-icon>
                <span>에서 날짜별 등록된 모임을 확인하고, </span>
                <v-icon style="padding-bottom: 2px" small>
                  mdi-dots-vertical
                </v-icon>
                <span>를 클릭하면 </span>
                <span class="yellow-line">모임 상세페이지</span>
                <span>로 이동합니다.</span>
                <br />
                <span>모임 상세페이지에서 우측 상단에</span>
                <v-icon class="mx-1" style="padding-bottom: 2px" small>
                  $vuetify.icons.user_invite_outline
                </v-icon>
                <span
                  >를 클릭해 보세요. 아래와 같은 창을 보실 수 있습니다.</span
                >
                <v-sheet class="d-flex flex-row justify-center align-center">
                  <v-sheet
                    rounded
                    class="mx-2 px-5 mt-4"
                    max-width="280"
                    elevation="2"
                  >
                    <v-card-title class="d-flex flex-column">
                      <span
                        class="point-font md-font main-col-1 seminarrow-font"
                        >모임 제목</span
                      >
                      <span class="point-font xxxl-ont main-col-1"
                        >초대 링크</span
                      >
                      <img
                        src="@/assets/images/dialog/invite_link.png"
                        width="50%"
                      />
                      <v-sheet
                        class="pr-1 px-4 detail-border d-flex flex-row justify-space-between align-center"
                        rounded="lg"
                        width="100%"
                        height="30"
                      >
                        <span class="xxxs-font light-font">초대 링크 URL</span>
                        <div>
                          <v-btn icon>
                            <v-icon small>$vuetify.icons.copy_outline</v-icon>
                          </v-btn>
                          <v-btn icon>
                            <v-icon small>$vuetify.icons.share_outline</v-icon>
                          </v-btn>
                        </div>
                      </v-sheet>
                      <span class="extralight-font xxxxs-font main-col-1">
                        초대 링크를 공유해 멤버를 추가해 보세요.
                      </span>
                    </v-card-title>
                  </v-sheet>
                </v-sheet>
                <br />
                <span>
                  링크를 복사하거나 카카오톡 공유하기 버튼으로 초대 링크를 다른
                  사람에게 보내보세요.
                </span>
                <span>
                  초대 링크는 모임을 등록한 방장만 볼 수 있습니다. 뿐만 아니라
                  모임 삭제 및 수정, 정산 내역 삭제도 방장만 가능합니다.
                </span>
              </div>
            </div>
            <div class="d-flex flex-column mt-3">
              <v-chip color="var(--green-col)" dark>
                <span class="mr-2 black-font">STEP 3</span>
                <span>나의 출발지 정하기</span>
              </v-chip>
              <div class="pa-2 light-font xs-font main-col-1">
                <span>모임에 입장하면 모든 멤버는 </span>
                <span class="green-line">모임 상세페이지</span>
                <span>에서 </span>
                <span class="green-line">자신의 출발 위치</span>
                <span>를 지정할 수 있습니다.</span>
                <br />
                <v-sheet class="mx-2 pa-4 mt-4" elevation="2" rounded>
                  <v-sheet
                    class="d-flex flex-row justify-space-between align-end"
                  >
                    <span class="point-font xxl-font main-col-1">
                      나의 출발지
                    </span>
                    <v-btn
                      class="d-flex justify-center align-center"
                      id="square-btn"
                      outlined
                      color="var(--main-col-1)"
                      rounded
                      small
                    >
                      <v-icon>$vuetify.icons.edit_outline</v-icon>
                    </v-btn>
                  </v-sheet>
                  <v-sheet
                    class="my-1 py-2 px-4 d-flex flex-column justify-center detail-border main-col-1"
                    rounded="lg"
                  >
                    <span class="xxs-font">출발지를 설정하지 않았습니다.</span>
                  </v-sheet>
                </v-sheet>
                <br />
                <span>우측 상단의 </span>
                <v-icon small>$vuetify.icons.edit_outline</v-icon>
                <span>를 눌러 나의 출발지를 설정해 보세요.</span>
                <span>
                  다른 멤버들이 내가 어디서 출발하는지 알 수 있을 뿐만 아니라
                  중간 위치를 찾을 때 내가 설정한 출발지가 자동으로 세팅됩니다.
                </span>
              </div>
            </div>
            <div class="d-flex flex-column mt-3">
              <v-chip color="var(--blue-col)" dark>
                <span class="mr-2 black-font">STEP 4</span>
                <span>정산하기</span>
              </v-chip>
              <div class="pa-2 light-font xs-font main-col-1">
                <span>
                  저희 서비스는 모임에서 쓰인 비용을 1/N 하는 것을 도와주는
                  기능도 지원합니다. 모임 정산을 보다 편리하게 진행해 보세요!
                </span>
                <v-sheet class="my-2">
                  <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                    1
                  </v-chip>
                  <span class="bold-font main-col-1">지각비 설정하기</span>
                </v-sheet>
                <span>모임 상세페이지에서 지각비를 설정해 보세요. </span>
                <span>
                  실시간 위치 공유에서 모임 시간 내 도착완료를 누르지 않은
                  사람은 지각이 됩니다. 지각한 사람에게는 지각비가 자동으로
                  부과됩니다.
                </span>
                <br />
                <div class="d-flex flex-row justify-end">
                  <v-chip color="var(--main-col-2)" x-small dark>
                    *도착완료에 대한 내용은 실시간 위치 탭에서 확인해 주세요.
                  </v-chip>
                </div>
                <v-sheet class="mx-2 pa-4 mt-4" elevation="2" rounded>
                  <v-sheet
                    class="d-flex flex-row justify-space-between align-end"
                  >
                    <span class="point-font xxl-font main-col-1"> 지각비 </span>
                  </v-sheet>
                  <v-sheet
                    class="my-1 py-2 px-4 d-flex flex-column justify-center detail-border main-col-1"
                    rounded="lg"
                  >
                    <span class="xxs-font">지각비를 설정하지 않았습니다.</span>
                  </v-sheet>
                </v-sheet>
                <br />
                <span>
                  지각비는 방장만 바꿀 수 있으며 모임 상세페이지 우측 상단의
                </span>
                <v-icon small>$vuetify.icons.edit_outline</v-icon>
                <span>를 클릭해 모임 정보를 수정 시 바꿀 수 있습니다.</span>
                <v-sheet class="my-2">
                  <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                    2
                  </v-chip>
                  <span class="bold-font main-col-1">영수증 등록하기</span>
                </v-sheet>
                <span>모임 상세페이지 맨 아래 정산에서 </span>
                <v-btn
                  color="var(--main-col-1)"
                  elevation="0"
                  rounded
                  x-small
                  dark
                >
                  영수증 추가
                </v-btn>
                <span> 버튼을 눌러 영수증을 등록해 보세요. </span>
                <span>영수증의 상호명과 총액을 인식해 보여줍니다.</span>
                <v-sheet class="my-2">
                  <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                    3
                  </v-chip>
                  <span class="bold-font main-col-1">
                    정산 내역 확인 및 공유하기
                  </span>
                </v-sheet>
                <span>
                  등록된 영수증의 총액과 설정된 지각비를 바탕으로 개인별 내야
                  하는 금액을 보여줍니다.
                </span>
                <v-sheet class="mx-2 pa-4 mt-4" elevation="2" rounded>
                  <v-sheet
                    class="d-flex flex-row justify-space-between align-center"
                  >
                    <span class="point-font xxl-font main-col-1"> 정산 </span>
                    <v-btn
                      class="ml-2 justify-space-between"
                      depressed
                      x-small
                      color="var(--main-col-1)"
                      dark
                      rounded
                    >
                      <v-icon class="mr-2" color="white" small
                        >mdi-share-variant-outline</v-icon
                      >
                      <span class="xxxs-font">카카오톡 공유하기</span>
                    </v-btn>
                  </v-sheet>
                  <v-simple-table dense>
                    <template v-slot:default>
                      <tbody>
                        <tr
                          class="d-flex flex-row justify-space-between align-center"
                          style="cursor: pointer"
                        >
                          <td
                            class="px-1 d-flex align-center light-font main-col-1 xxs-font"
                            style="border-bottom: none"
                          >
                            메머드 익스프레스
                          </td>
                          <td
                            class="px-1 d-flex align-center light-font main-col-1 xxs-font"
                            style="border-bottom: none"
                          >
                            10000원
                          </td>
                        </tr>
                        <tr
                          class="d-flex flex-row justify-space-between align-center xxs-font"
                          style="cursor: pointer"
                        >
                          <td
                            class="px-1 d-flex align-center light-font main-col-1 xxs-font"
                            style="border-bottom: none"
                          >
                            스타벅스 역삼점
                          </td>
                          <td
                            class="px-1 d-flex align-center light-font main-col-1 xxs-font"
                            style="border-bottom: none"
                          >
                            20000원
                          </td>
                        </tr>
                      </tbody>
                    </template>
                  </v-simple-table>
                  <v-divider class="mb-1"></v-divider>
                  <div class="d-flex flex-row justify-space-between">
                    <span class="px-1 pb-1 medium-font xxs-font">합계</span>
                    <span class="px-1 pb-1 medium-font xxs-font">30000원</span>
                  </div>
                  <div class="d-flex flex-row justify-space-between">
                    <span class="px-1 pb-1 medium-font xxs-font">잔액</span>
                    <span class="px-1 pb-1 medium-font xxs-font">100원</span>
                  </div>
                  <div class="d-flex flex-row justify-space-between">
                    <span class="px-1 light-font xxs-font">지각비</span>
                    <span class="px-1 light-font xxs-font">1000원</span>
                  </div>
                  <v-divider class="my-1"></v-divider>
                  <div class="d-flex flex-row justify-space-between">
                    <span class="px-1 bold-font xxs-font">
                      내가 내야 하는 금액
                    </span>
                    <span class="px-1 bold-font xxs-font">11000원</span>
                  </div>
                </v-sheet>
                <br />
                <span>
                  정산 내용을 다른 사람에게 공유하고 싶다면 우측 상단의
                </span>
                <v-btn
                  class="justify-space-between"
                  depressed
                  x-small
                  color="var(--main-col-1)"
                  dark
                  rounded
                >
                  <v-icon class="mr-2" color="white" small>
                    mdi-share-variant-outline
                  </v-icon>
                  <span class="xxxs-font">카카오톡 공유하기</span>
                </v-btn>
                <span> 버튼을 눌러보세요.</span>
              </div>
            </div>
          </div>
        </v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 110px 25px 65px 25px">
          <div
            class="mb-2 d-flex flex-column point-font main-col-1 xxxxl-font semi2narrow-font"
          >
            <span>멤버 간 중간 위치를 찾아</span>
            <span>모임 장소로 등록하세요!</span>
            <v-divider
              class="mt-2"
              width="300"
              style="border-color: var(--main-col-1)"
            ></v-divider>
          </div>
          <div class="light-font xs-font mt-4 main-col-1">
            <span>
              모임 장소를 정할 때 중간 위치를 찾아주는 기능을 사용하실 수
              있습니다.
            </span>
            <span class="orange-line">모임 등록</span>
            <span> 또는 </span>
            <span class="yellow-line">모임 수정 창</span>
            <span>에서 </span>
            <span>장소 입력창을 클릭해 </span>
            <span class="green-line">장소 선택 페이지</span>
            <span>로 이동해 주세요.</span>
            <br />
            <span>우측 상단의 </span>
            <v-btn color="var(--main-col-1)" outlined rounded x-small>
              <v-icon class="mr-1" x-small>
                $vuetify.icons.location_outline
              </v-icon>
              <span>중간 위치 찾기</span>
            </v-btn>
            <span> 버튼을 클릭해 주세요. </span>
            <v-sheet class="d-flex justify-center">
              <v-sheet
                class="mx-2 px-4 mt-4 py-4 d-flex flex-column align-center"
                elevation="2"
                max-width="300"
                rounded
              >
                <img
                  class="mb-2"
                  src="@/assets/images/dialog/earth.png"
                  width="20%"
                  alt=""
                />
                <span class="regular-font xxs-font semi2narrow-font">
                  출발지를 입력해
                </span>
                <span class="regular-font xxs-font">
                  중간 위치를 추천 받아보세요!
                </span>
                <v-sheet
                  class="mt-2 py-1 px-3 detail-border xxs-font d-flex flex-row justify-start align-center"
                  width="100%"
                  rounded="lg"
                >
                  <span class="main-col-1 medium-font">
                    1. 출발지를 입력하세요!
                  </span>
                </v-sheet>
                <v-sheet
                  class="mt-2 py-1 px-3 detail-border xxs-font d-flex flex-row justify-start align-center"
                  width="100%"
                  rounded="lg"
                >
                  <span class="main-col-1 medium-font">
                    2. 출발지를 입력하세요!
                  </span>
                </v-sheet>
                <v-btn class="mt-2 main-col-1" text small block rounded>
                  <v-icon class="mr-1" small>$vuetify.icons.point</v-icon>
                  <span>출발지 추가하기</span>
                </v-btn>
                <v-btn
                  class="mt-2"
                  color="var(--main-col-1)"
                  elevation="0"
                  block
                  small
                  dark
                  rounded
                >
                  중간 위치 찾기
                </v-btn>
              </v-sheet>
            </v-sheet>
            <br />
            <v-btn elevation="0" color="var(--main-col-1)" outlined x-small>
              1. 출발지를 입력하세요!
            </v-btn>
            <span> 를 클릭하시면 출발지를 설정할 수 있습니다. </span>
            <span>만약 2개 이상의 출발지를 입력하고 싶으시다면 </span>
            <v-btn class="main-col-1" text x-small rounded>
              <v-icon class="mr-1" small>$vuetify.icons.point</v-icon>
              <span>출발지 추가하기</span>
            </v-btn>
            <span>
              버튼을 클릭해 출발지를 추가하세요. 만약 팀원들의 출발지가 설정돼
              있다면 자동으로 추가됩니다.
            </span>
            <br />
            <span>출발지를 모두 입력했다면 </span>
            <v-btn color="var(--main-col-1)" elevation="0" x-small dark rounded>
              중간 위치 찾기
            </v-btn>
            <span> 를 클릭해 중간 위치를 찾아보세요.</span>
            <v-sheet class="d-flex justify-center mt-4" rounded>
              <v-sheet width="90%" max-width="300" elevation="2" rounded>
                <img
                  width="100%"
                  height="100%"
                  src="@/assets/images/page/halfway.jpg"
                  style="border-radius: 6px"
                />
              </v-sheet>
            </v-sheet>
            <br />
            <span>왼쪽 위의 버튼 </span>
            <v-btn-toggle
              rounded
              background-color="white"
              color="var(--main-col-1)"
            >
              <v-btn
                color="var(--main-col-1)"
                x-small
                outlined
                style="
                  width: 8px;
                  min-width: 8px;
                  border-color: var(--main-col-1) !important;
                "
              >
                <v-icon color="var(--main-col-1)" small
                  >mdi-subway-variant</v-icon
                >
              </v-btn>
              <v-btn
                color="var(--main-col-1)"
                x-small
                outlined
                style="
                  width: 8px;
                  min-width: 8px;
                  border-color: var(--main-col-1) !important;
                "
              >
                <v-icon color="var(--main-col-1)" small
                  >mdi-silverware-fork-knife</v-icon
                >
              </v-btn>
              <v-btn
                color="var(--main-col-1)"
                x-small
                outlined
                style="
                  width: 8px;
                  min-width: 8px;
                  border-color: var(--main-col-1) !important;
                "
              >
                <v-icon color="var(--main-col-1)" small>mdi-coffee</v-icon>
              </v-btn>
              <v-btn
                color="var(--main-col-1)"
                x-small
                outlined
                style="
                  width: 8px;
                  min-width: 8px;
                  border-color: var(--main-col-1) !important;
                "
              >
                <v-icon color="var(--main-col-1)" small>mdi-movie-play</v-icon>
              </v-btn>
            </v-btn-toggle>
            <span>
              중 하나를 선택해 클릭해 주세요. 왼쪽부터 지하철역, 음식점, 카페,
              영화관을 추천해 드립니다.
            </span>
            <br />
            <span>지도의 </span>
            <v-icon color="var(--yellow-col)">mdi-map-marker-star</v-icon>
            <span>는 중간 위치를, </span>
            <img
              style="vertical-align: bottom"
              width="18"
              class="mb-1"
              src="@/assets/images/icons/marker1.png"
            />
            <span> 는 출발지를 가리킵니다. </span>
            <v-icon color="var(--yellow-col)">mdi-map-marker-star</v-icon>
            <span> 를 클릭하면 아래와 같은 마커를 보실 수 있습니다. </span>
            <v-sheet
              width="100%"
              class="pa-1 mt-4 d-flex justify-center"
              rounded
            >
              <v-sheet
                class="d-flex flex-column"
                elevation="2"
                width="100%"
                max-width="350"
                rounded
              >
                <v-sheet
                  class="px-3 py-2 white-font rounded-t d-flex flex-row justify-space-between align-center"
                  color="var(--main-col-1)"
                >
                  <span class="bold-font">학동역 7호선</span>
                  <v-icon color="white">mdi-chevron-right</v-icon>
                </v-sheet>
                <div class="d-flex flex-row align-end justify-space-between">
                  <v-sheet class="px-3 py-2 d-flex flex-column" rounded>
                    <span class="xxs-font">서울 강남구 학동로 지하 180</span>
                    <span class="xxxxs-font main-col-2">
                      (지번 : 서울 강남구 논현동 279-67)
                    </span>
                    <span class="xxs-font">02-6311-7311</span>
                  </v-sheet>
                  <div>
                    <img
                      class="mx-1"
                      width="30"
                      src="@/assets/images/icons/bus-icon.png"
                    />
                    <img
                      class="ml-1 mr-2"
                      width="30"
                      src="@/assets/images/icons/car-icon.png"
                    />
                  </div>
                </div>
              </v-sheet>
            </v-sheet>
            <br />
            <span>아래 마커에서 우측 하단의 </span>
            <img
              class="mr-1"
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/bus-icon.png"
            />
            <img
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/car-icon.png"
            />
            <span>
              를 클릭하면 각 출발지로부터 선택한 위치까지의 경로를 볼 수
              있습니다.
            </span>
            <img
              class="mr-1"
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/bus-icon.png"
            />
            <span>는 대중교통으로 이동 시, </span>
            <img
              class="mr-1"
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/car-icon.png"
            />
            <span>는 자동차로 이동 시 경로입니다. </span>
            <v-sheet class="d-flex justify-center" rounded>
              <v-sheet
                class="mt-4 py-3 px-4 d-flex flex-column"
                elevation="2"
                width="90%"
                max-width="350"
                rounded
              >
                <v-sheet
                  class="mb-2 d-flex flex-row align-center justify-space-between"
                >
                  <div>
                    <span class="md-font bold-font">학동역 7호선</span>
                    <div class="d-flex flex-row align-center">
                      <span class="mr-2 sm-font bold-font">10분</span>
                      <span class="xxxxs-font light-font main-col-2">
                        평균 이동 시간
                      </span>
                    </div>
                  </div>
                  <img
                    class="mr-1"
                    width="30"
                    style="vertical-align: bottom"
                    src="@/assets/images/icons/bus-icon.png"
                  />
                </v-sheet>
                <v-sheet
                  class="mb-1 mr-2 d-flex flex-row justify-space-between"
                >
                  <v-sheet
                    class="d-flex flex-row justify-space-between align-center"
                    width="45%"
                  >
                    <img
                      style="vertical-align: bottom"
                      width="25"
                      class="mb-1"
                      src="@/assets/images/icons/marker1.png"
                    />
                    <span class="medium-font">11분</span>
                  </v-sheet>
                  <v-sheet
                    class="d-flex flex-row justify-space-between align-center"
                    width="45%"
                  >
                    <img
                      style="vertical-align: bottom"
                      width="25"
                      class="mb-1"
                      src="@/assets/images/icons/marker2.png"
                    />
                    <span class="medium-font">9분</span>
                  </v-sheet>
                </v-sheet>
                <v-sheet
                  class="d-flex flex-row justify-space-between align-center"
                >
                  <div class="main-col-2 xxs-font">상세보기</div>
                  <v-btn
                    color="var(--main-col-1)"
                    elevation="0"
                    dark
                    rounded
                    small
                  >
                    선택
                  </v-btn>
                </v-sheet>
              </v-sheet>
            </v-sheet>
            <br />
            <img
              class="mr-1"
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/bus-icon.png"
            />
            <span>또는 </span>
            <img
              width="20"
              style="vertical-align: bottom"
              src="@/assets/images/icons/car-icon.png"
            />
            <span>
              중 하나를 선택하면 하단에 위와 같은 창을 보실 수 있습니다. 여기서
            </span>
            <img
              style="vertical-align: bottom; margin-top: 2px"
              width="18"
              src="@/assets/images/icons/marker1.png"
            />
            <span>
              옆의 시간은 선택된 장소인 '학동역 7호선'으로부터 첫 번째 입력했던
              출발지까지의 소요 시간입니다.
            </span>
            <br />
            <span>
              '상세보기'를 누르면 '학동역 7호선'에 대한 자세한 정보를 확인할 수
              있고,
            </span>
            <v-btn class="mb-1" color="var(--main-col-1)" rounded x-small dark>
              선택
            </v-btn>
            <span>
              을 누르면 모임 장소 입력칸에 '학동역 7호선'이 기입됩니다.
            </span>
          </div>
        </v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 110px 25px 65px 25px">
          <div
            class="mb-2 d-flex flex-column point-font main-col-1 xxxxl-font semi2narrow-font"
          >
            <span>모임 내에서 실시간으로</span>
            <span>위치를 공유하고 채팅을 해보세요!</span>
            <v-divider
              class="mt-2"
              width="320"
              style="border-color: var(--main-col-1)"
            ></v-divider>
          </div>
          <div class="light-font xs-font mt-4 main-col-1">
            <span>모임 멤버들과 모임 생성 직후부터 </span>
            <span class="orange-line">실시간 채팅</span>
            <span>을 할 수 있습니다. 모임 3시간 전후로는 </span>
            <span class="yellow-line">실시간 위치 공유</span>
            <span>를 통해 서로의 위치를 확인할 수 있습니다.</span>
            <br />
          </div>
          <div class="light-font xs-font mt-2 main-col-1">
            <span>모임 상세페이지에서 모임 제목 옆에 위치한 </span>
            <v-icon small>$vuetify.icons.map_outline</v-icon>
            <span>
              아이콘을 클릭하면 실시간 위치 공유로 이동할 수 있습니다.
            </span>
            <span>홈페이지의 오늘의 모임에서는 </span>
            <v-icon small>$vuetify.icons.map_outline</v-icon>
            <span> 아이콘을 선택하시면 됩니다. </span>
            <span>다만, 실시간 위치 공유는 </span>
            <span class="orange-line">모임 시간 3시간 전후로만</span>
            <span> 가능합니다.</span>
            <br />
            <v-sheet
              class="mx-2 my-4 d-flex flex-row justify-center"
              rounded
              elevation="2"
            >
              <img
                style="border-radius: 6px 0px 0px 6px"
                width="50%"
                src="@/assets/images/page/live_map1.jpg"
              />
              <img
                style="border-radius: 0px 6px 6px 0px"
                width="50%"
                src="@/assets/images/page/live_map2.jpg"
              />
            </v-sheet>
            <div class="mb-1">
              <span>지도는 위와 같이 나타나며 </span>
              <span class="yellow-line">우측 상단 스위치</span>
              <span>
                를 클릭해 배경을 다르게 볼 수 있습니다. 모임 3시간 전후
              </span>
              <span class="yellow-line">최신 채팅</span>
              <span>이 각 멤버 캐릭터 상단에 나타납니다.</span>
            </div>
            <v-sheet
              class="mt-3 mb-4 d-flex justify-center"
              color="transparent"
            >
              <v-bottom-navigation
                color="primary"
                style="border: 1px solid var(--main-col-1); border-radius: 10px"
                max-width="480"
                width="90%"
                height="45"
              >
                <v-btn>
                  <span class="main-col-1 xxxxs-font">채팅</span>
                  <v-icon color="var(--main-col-1)">mdi-forum</v-icon>
                </v-btn>
                <v-btn>
                  <span class="main-col-1">도착 완료</span>
                  <v-icon color="var(--main-col-1)"
                    >mdi-map-marker-check</v-icon
                  >
                </v-btn>
                <v-btn>
                  <span class="main-col-1">범위 재조정</span>
                  <v-icon color="var(--main-col-1)"
                    >mdi-arrow-expand-all</v-icon
                  >
                </v-btn>
              </v-bottom-navigation>
            </v-sheet>
            <v-sheet class="my-2 d-flex flex-row align-center">
              <v-chip class="mr-2" color="var(--red-col)" dark small>
                1
              </v-chip>
              <span class="md-font bold-font red-font">실시간 채팅</span>
            </v-sheet>
            <span>
              해당 페이지 외에도 모임 상세페이지에서 모임 제목 옆에 위치한
            </span>
            <v-icon small>$vuetify.icons.chatting_outline</v-icon>
            <span>
              아이콘을 클릭하면 실시간 채팅으로 이동해 모임 멤버들과 채팅을 나눌
              수 있습니다.
            </span>
            <v-sheet class="my-3 d-flex flex-row justify-center">
              <v-sheet class="pa-3" width="93%" elevation="2" rounded>
                <div class="d-flex flex-row align-start justify-start">
                  <v-avatar class="mr-2" size="34" rounded="lg">
                    <v-img
                      src="https://cdn.mediafine.co.kr/news/photo/202304/28522_49668_359.jpg"
                    ></v-img>
                  </v-avatar>
                  <div>
                    <div class="xxxxs-font">안유진</div>
                    <div class="d-flex flex-row align-end">
                      <v-sheet
                        class="mr-1 pa-1 px-2 xxs-font light-font d-flex flex-row"
                        outlined
                        elevation="0"
                        rounded="lg"
                      >
                        안녕
                      </v-sheet>
                      <span class="xxxxxs-font main-col-2">오후 6시 22분</span>
                    </div>
                  </div>
                </div>
                <div class="mt-2 d-flex flex-row align-start justify-start">
                  <v-avatar class="mr-2" size="34" rounded="lg">
                    <v-img
                      src="https://img.seoul.co.kr//img/upload/2022/10/05/SSI_20221005160317_V.jpg"
                    ></v-img>
                  </v-avatar>
                  <div>
                    <div class="xxxxs-font">장원영</div>
                    <div class="d-flex flex-row align-end">
                      <v-sheet
                        class="mr-1 pa-1 px-2 xxs-font light-font d-flex flex-row"
                        outlined
                        elevation="0"
                        rounded="lg"
                      >
                        안녕안녕
                      </v-sheet>
                      <span class="xxxxxs-font thin-font main-col-2">
                        오후 6시 40분
                      </span>
                    </div>
                  </div>
                </div>
                <div
                  class="mt-2 d-flex flex-row-reverse align-start justify-start"
                >
                  <div class="d-flex flex-column align-end">
                    <div class="d-flex flex-row align-end">
                      <span class="xxxxxs-font thin-font main-col-2">
                        오후 6시 40분
                      </span>
                      <v-sheet
                        class="ml-1 pa-1 px-2 xxs-font light-font white-font d-flex flex-row"
                        outlined
                        elevation="0"
                        rounded="lg"
                        color="var(--main-col-1)"
                      >
                        안녕안녕
                      </v-sheet>
                    </div>
                  </div>
                </div>
                <v-sheet
                  class="mt-3 d-flex flex-row justify-space-between"
                  color="transparent"
                >
                  <v-sheet
                    width="100%"
                    class="mr-2 px-2 py-1 xxs-font main-col-2"
                    outlined
                    rounded
                  >
                    메세지를 입력해주세요.
                  </v-sheet>
                  <v-icon>$vuetify.icons.send_outline</v-icon>
                </v-sheet>
              </v-sheet>
            </v-sheet>
            <v-sheet class="my-2 d-flex flex-row align-center">
              <v-chip class="mr-2" color="var(--yellow-col)" dark small>
                2
              </v-chip>
              <span class="md-font bold-font yellow-font">도착 완료</span>
            </v-sheet>
            <span>도착 장소 100m 이내에 도달하면 </span>
            <span class="orange-line">도착완료 버튼을 클릭해 도착 처리</span>
            <span>를 할 수 있습니다. </span>
            <span>모임 시간 내 </span>
            <span>도착완료 버튼을 클릭하지 않으면 </span>
            <span class="yellow-line">지각 처리</span>
            <span>가 되고 지각비가 부과되니 유의해 주세요!</span>
            <v-sheet class="my-2 d-flex flex-row align-center">
              <v-chip class="mr-2" color="var(--green-col)" dark small>
                3
              </v-chip>
              <span class="md-font bold-font green-font">범위 재조정</span>
            </v-sheet>
            <span>범위 재조정 버튼을 클릭하면 </span>
            <span class="green-line">접속해 있는 멤버 전체가 보이도록</span>
            <span> 지도의 확대 비율을 조절해줍니다.</span>
          </div>
        </v-sheet>
      </v-tab-item>
    </v-tabs-items>
    <v-bottom-navigation
      fixed
      style="margin: 0 auto; left: 0; right: 0; box-shadow: none"
      max-width="500"
      height="50"
    >
      <v-btn
        id="manual-btn"
        class="sm-font point-font"
        color="var(--main-col-1)"
        style="color: white !important"
        block
        @click="goRegister()"
      >
        모임 등록하러가기
      </v-btn>
    </v-bottom-navigation>
  </v-sheet>
</template>

<script>
import NavHeader from "@/views/Header/NavHeader.vue";
export default {
  components: { NavHeader },
  name: "ManualPage",
  data() {
    return {
      tab: 0,
      items: ["소개", "모임", "중간장소", "실시간위치"],
    };
  },
  methods: {
    goRegister() {
      this.$router.push("/register");
    },
  },
};
</script>

<style>
.v-tabs:not(.v-tabs--vertical):not(.v-tabs--right)
  > .v-slide-group--is-overflowing.v-tabs-bar--is-mobile:not(
    .v-slide-group--has-affixes
  )
  .v-slide-group__prev {
  display: none !important;
}
.red-line {
  box-shadow: inset 0 -16px 0 var(--roulette-col-6);
}
.orange-line {
  box-shadow: inset 0 -16px 0 var(--roulette-col-7);
}
.yellow-line {
  box-shadow: inset 0 -16px 0 var(--roulette-col-8);
}
.green-line {
  box-shadow: inset 0 -16px 0 var(--roulette-col-9);
}
.blue-line {
  box-shadow: inset 0 -16px 0 var(--roulette-col-10);
}
</style>
