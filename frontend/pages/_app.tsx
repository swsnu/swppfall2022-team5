import '../styles/globals.css'
import type { AppProps } from 'next/app'
import Script from 'next/script'

export default function App({ Component, pageProps }: AppProps) {

  return (
    <>
      <Script
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=186c8188c6305d81e1c1b9407a925f33&libraries=services,clusterer&autoload=false"
        strategy="beforeInteractive"
      />
      <Component {...pageProps} />
    </>
  )
}
