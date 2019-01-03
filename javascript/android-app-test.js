import 'babel-polyfill'
import 'colors'
import wd from 'wd'
import {assert} from 'chai'

const username = process.env.KOBITON_USERNAME
const apiKey = process.env.KOBITON_API_KEY

const deviceUdid = process.env.KOBITON_DEVICE_UDID || ''
const deviceName = process.env.KOBITON_DEVICE_NAME || 'Galaxy*'
const deviceOrientation = process.env.KOBITON_SESSION_DEVICE_ORIENTATION || 'portrait'
const captureScreenshots = process.env.KOBITON_SESSION_CAPTURE_SCREENSHOTS || true
const deviceGroup = process.env.KOBITON_SESSION_DEVICE_GROUP || 'KOBITON'
const app = process.env.KOBITON_SESSION_APPLICATION_URL || 'https://appium.github.io/appium/assets/ApiDemos-debug.apk'
const platformVersion = process.env.KOBITON_SESSION_PLATFORM_VERSION || ''
const groupId = process.env.KOBITON_SESSION_GROUP_ID || ''

const kobitonServerConfig = {
  protocol: 'https',
  host: 'api.kobiton.com',
  auth: `${username}:${apiKey}`
}

const desiredCaps = {
  sessionName:        'Automation test session',
  sessionDescription: 'This is an example for Android app',
  deviceOrientation:  deviceOrientation,
  captureScreenshots: captureScreenshots,
  deviceGroup:        deviceGroup,
  platformName:       'Android',
  app: app
}

if (deviceUdid) {
  desiredCaps['deviceUdid'] = deviceUdid
}
else {
  desiredCaps['deviceName'] = deviceName
  desiredCaps['platformVersion'] = platformVersion
}

if (groupId) {
  desiredCaps['groupId'] = groupId
}

let driver

if (!username || !apiKey) {
  console.log('Error: Environment variables KOBITON_USERNAME and KOBITON_API_KEY are required to execute script')
  process.exit(1)
}

describe('Android App sample', () => {

  before(async () => {
    driver = wd.promiseChainRemote(kobitonServerConfig)

    driver.on('status', (info) => {
      console.log(info.cyan)
    })
    driver.on('command', (meth, path, data) => {
      console.log(' > ' + meth.yellow, path.grey, data || '')
    })
    driver.on('http', (meth, path, data) => {
      console.log(' > ' + meth.magenta, path, (data || '').grey)
    })

    try {
      await driver.init(desiredCaps)
    }
    catch (err) {
      if (err.data) {
        console.error(`init driver: ${err.data}`)
      }
    throw err
    }
  })

  it('should show the app label', async () => {
    await driver.elementByClassName("android.widget.TextView")
      .text().then(function(text) {
        assert.equal(text.toLocaleLowerCase(), 'api demos')
      })
  })

  after(async () => {
    if (driver != null) {
    try {
      await driver.quit()
    }
    catch (err) {
      console.error(`quit driver: ${err}`)
    }
  }
  })
})
