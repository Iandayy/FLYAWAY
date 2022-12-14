import { selectedVideoState } from '../../recoil/atoms/videoState';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { memberIdState } from '../../recoil/atoms/memberIdState';
import { authorizationState } from '../../recoil/atoms/authorizationState';
import YouTube from 'react-youtube';
import instance from '../../service/request';

export default function VideoDetail() {
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const memberId = useRecoilValue(memberIdState);
  const token = useRecoilValue(authorizationState);
  const apiURL = `/record/${memberId}`;
  const headers = {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  };
  const video = useRecoilValue(selectedVideoState)[0];
  const viewCount = video.statistics.viewCount.replace(
    /\B(?=(\d{3})+(?!\d))/g,
    ','
  );
  const date = video.snippet.publishedAt;
  const YYYY = date.slice(0, 4);
  const MM = date.slice(5, 7);
  const DD = date.slice(8, 10);
  const likeCount = video.statistics.likeCount.replace(
    /\B(?=(\d{3})+(?!\d))/g,
    ','
  );

  let startDate;
  let stopDate;
  let sec;

  function onPlay() {
    startDate = new Date();
  }

  function onPause() {
    stopDate = new Date();
    sec = (stopDate.getTime() - startDate.getTime()) / 1000;
    instance.post(apiURL, { record: sec }, headers);
  }

  function onEnd() {
    stopDate = new Date();
    sec = (stopDate.getTime() - startDate.getTime()) / 1000;
    instance.post(apiURL, { record: sec }, headers);
  }

  return (
    <section className="w-full">
      <YouTube
        id="player"
        title="ytplayer"
        type="text/html"
        videoId={video.id}
        opts={{
          width: '100%',
          height: '400vh',
          playerVars: {
            rel: 0, //방금 동영상이 재생된 채널에서 관련 동영상을 가져옴
            modestbranding: 1, // 컨트롤 바에 youtube 로고를 표시하지 않음
          },
          origin: 'http://localhost:3000',
        }}
        onPlay={onPlay}
        onPause={onPause}
        onEnd={onEnd}
      />
      <div id="videoInfo">
        <span>조회수 {viewCount}회</span>﹒
        <span>
          업로드 {YYYY}.{MM}.{DD}.
        </span>
        ﹒<span>♥️ {likeCount}</span>
      </div>
      <h2 className="mt-2 text-3xl sm:text-xl">{video.snippet.title}</h2>
      <h3 className="mt-1 sm:text-sm">{video.snippet.channelTitle}</h3>
      <pre className="mt-3 whitespace-pre-wrap">
        {video.snippet.description}
      </pre>
    </section>
  );
}
