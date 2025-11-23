#!/usr/bin/env bash
set -euo pipefail

# === 설정 ===
FEATURE_NAME="crud-bulletin-board"
REPO_NAME="$(basename "$(git rev-parse --show-toplevel)")"

echo "🚀 Feature: ${FEATURE_NAME}"
echo "📁 Repo: ${REPO_NAME}"
echo

# GitHub CLI가 로그인 되어 있는지 간단 체크
if ! gh auth status >/dev/null 2>&1; then
  echo "❌ gh 인증이 필요합니다. 'gh auth login'을 먼저 실행하세요."
  exit 1
fi

# jules CLI 체크
if ! command -v jules >/dev/null 2>&1; then
  echo "❌ jules CLI가 설치되어 있지 않습니다. 'npm install -g @google/jules' 등을 확인하세요."
  exit 1
fi

if ! command -v jq >/dev/null 2>&1; then
  echo "❌ jq가 필요합니다. 'brew install jq' 등으로 먼저 설치하세요."
  exit 1
fi

# === 작업 정의 ===
# 형식: "이슈 제목|jules에게 줄 요약 지시문"
tasks=(
  "Backend - 게시물 엔티티 및 DB 스키마 정의|Ktor 백엔드에서 게시물(Post) 엔티티와 Exposed 기반 DB 스키마를 정의해줘. Post는 id(Long), title(String), content(String 또는 Text), createdAt, updatedAt 필드를 갖고, dev 환경에서는 H2 또는 Postgres를 사용하고, 테스트에서는 Testcontainers로 DB를 띄우도록 DatabaseFactory를 구성해."
  "Backend - 게시물 생성(C) 및 전체 조회(R) API 구현|Ktor에서 POST /api/posts 로 게시물 생성, GET /api/posts 로 전체 조회 API를 구현해줘. 서비스 레이어(PostService)로 비즈니스 로직을 분리하고, Kotest + Testcontainers로 성공/실패/엣지 케이스를 포함한 통합 테스트를 작성해."
  "Backend - 게시물 단건 조회/수정/삭제(R/U/D) API 구현|Ktor에서 GET /api/posts/{id}, PUT /api/posts/{id}, DELETE /api/posts/{id} 엔드포인트를 추가해줘. 존재하지 않는 id일 때는 404를 반환하고, 수정/삭제에 대한 통합 테스트를 Kotest + Testcontainers로 Given-When-Then 패턴으로 작성해."
  "Frontend - Next.js 설정 및 게시물 목록 페이지 구현|Next.js(App Router) + TypeScript + Tailwind CSS를 사용해 프론트엔드 프로젝트를 초기 설정하고, / 경로에서 백엔드의 GET /api/posts 를 호출해 게시물 목록을 보여주는 페이지를 구현해줘. 로딩/에러 상태는 SWR 또는 React Query로 관리해."
  "Frontend - 게시물 작성 페이지 및 라우팅 구현|/posts/new 페이지에서 제목/내용을 입력받아 POST /api/posts 를 호출해 새 게시물을 생성하는 기능을 구현해줘. 메인 목록에서 '새 글 작성' 버튼을 눌렀을 때 /posts/new 로 이동하고, 성공 시 목록으로 리다이렉트해."
  "Frontend - 게시물 상세/수정 페이지 구현|/posts/[id] 페이지에서 게시물 상세 정보를 GET /api/posts/{id} 로 불러와 보여주고, /posts/[id]/edit 페이지에서 기존 데이터를 불러와 수정 후 PUT /api/posts/{id} 를 호출해 업데이트하는 기능을 구현해줘. Next.js App Router 패턴을 따른다."
  "Frontend - 게시물 삭제 기능 및 UI 개선|목록과 상세 페이지에 삭제 버튼을 추가하고, 클릭 시 확인 모달을 띄운 뒤 DELETE /api/posts/{id} 를 호출해 삭제하는 기능을 구현해줘. 삭제 후 목록을 자동 갱신하도록 SWR/React Query 캐시를 무효화하고, 기본적인 UI/UX(버튼 상태, 에러 메시지)를 정리해."
)

# === 메인 루프 ===
for task in "${tasks[@]}"; do
  TITLE_PREFIX="[FEAT]"
  RAW_TITLE="${task%%|*}"
  TITLE="${TITLE_PREFIX} ${RAW_TITLE}"
  SUMMARY="${task#*|}"

  echo "========================================"
  echo "📝 작업: ${TITLE}"
  echo "----------------------------------------"

  # 1) GitHub 이슈 생성
  ISSUE_JSON="$(
    gh issue create \
      --title "${TITLE}" \
      --body "$(cat <<EOF
작업 목적  
${SUMMARY}

---

**완료 기준 (Definition of Done)**

- [ ] 요구사항에 맞게 코드가 구현되었다.
- [ ] 관련 테스트 코드가 추가/수정되었고, 모든 테스트가 통과한다.
- [ ] 기존 코드 스타일 및 프로젝트 컨벤션을 따른다.
- [ ] 필요 시 README나 문서가 업데이트되었다.
EOF
)" \
      --label "jules,feature" \
      --json number,title,url \
      --quiet
  )"

  ISSUE_NUMBER="$(echo "${ISSUE_JSON}" | jq -r '.number')"
  ISSUE_TITLE="$(echo "${ISSUE_JSON}" | jq -r '.title')"
  ISSUE_URL="$(echo "${ISSUE_JSON}" | jq -r '.url')"

  echo "✅ 이슈 생성 완료: #${ISSUE_NUMBER} - ${ISSUE_TITLE}"
  echo "🔗 ${ISSUE_URL}"

  # 2) 브랜치 이름 생성
  ISSUE_SLUG="$(echo "${RAW_TITLE}" | tr '[:upper:]' '[:lower:]' | sed -e 's/ /-/g' -e 's/[^a-z0-9-]//g')"
  BRANCH_NAME="feature/${FEATURE_NAME}-${ISSUE_NUMBER}-${ISSUE_SLUG}"

  echo "🌿 브랜치 이름: ${BRANCH_NAME}"
  echo "   (원하면: git checkout -b \"${BRANCH_NAME}\" && git push -u origin \"${BRANCH_NAME}\")"

  # 3) jules 세션 생성
  JULES_PROMPT=$(cat <<EOF
Issue #${ISSUE_NUMBER}: ${ISSUE_TITLE} (branch: ${BRANCH_NAME})

작업 목표:
${SUMMARY}

기술 스택 및 요구사항:
- Backend: Kotlin, Ktor, Coroutines, Exposed, Kotest, Testcontainers
- Frontend: Next.js(App Router), TypeScript, Tailwind CSS, SWR/React Query
- 공통:
  - 레포 루트 기준으로 관련 모듈/파일을 찾아서 수정하세요.
  - 기존 코드 구조(서비스 레이어, DI, 라우팅 구조 등)와 CLAUDE.md / README-JULES.md의 규칙을 따르세요.
  - 필요한 경우 새 테스트 파일을 만들되, Given-When-Then 패턴을 사용하세요.
  - PR 생성 시 커밋 메시지, 브랜치, PR 제목은 이 이슈에 맞게 생성하세요.
EOF
)

  echo
  echo "🤖 jules 세션 생성 중..."
  jules remote new --repo . --session "${JULES_PROMPT}"
  echo "✅ jules 세션 생성 완료"
  echo
done

echo "🎉 모든 작업에 대한 이슈/브랜치 이름/jules 세션 구성이 완료되었습니다."


