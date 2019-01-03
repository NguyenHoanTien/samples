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
const browserName = process.env.KOBITON_SESSION_BROWSER_NAME || 'chrome'
const platformVersion = process.env.KOBITON_SESSION_PLATFORM_VERSION || ''
const groupId = process.env.KOBITON_SESSION_GROUP_ID || ''


const kobitonServerConfig = {
  protocol: 'https',
  host: 'api.kobiton.com',
  auth: `${username}:${apiKey}`
}

const desiredCaps = {
  sessionName:        'Automation test session',
  sessionDescription: 'This is an example for Android web',
  deviceOrientation:  deviceOrientation,
  captureScreenshots: captureScreenshots,
  deviceGroup:        deviceGroup,
  browserName:        browserName, 
  platformName:       'Android'
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

describe('Android Web sample', () => {

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

  it('should return the title that contains Kobiton', async () => {
    await driver.get('https://www.google.com')
    .waitForElementByName('q')
    .sendKeys('Kobiton')
    .sleep(3000)
    .keys(wd.SPECIAL_KEYS.Enter)
    
    let msg = await driver.title()
    assert.include(msg, 'Kobiton - Google Search')
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
