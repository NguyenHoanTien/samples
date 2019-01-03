import 'babel-polyfill'
import 'colors'
import wd from 'wd'
import {assert} from 'chai'

const username = process.env.KOBITON_USERNAME
const apiKey = process.env.KOBITON_API_KEY

const deviceUdid = process.env.KOBITON_DEVICE_UDID || ''
const deviceName = process.env.KOBITON_DEVICE_NAME || 'iPhone*'
const deviceOrientation = process.env.KOBITON_SESSION_DEVICE_ORIENTATION || 'portrait'
const captureScreenshots = process.env.KOBITON_SESSION_CAPTURE_SCREENSHOTS || true
const deviceGroup = process.env.KOBITON_SESSION_DEVICE_GROUP || 'KOBITON'
const app = process.env.KOBITON_SESSION_APPLICATION_URL || 'https://s3-ap-southeast-1.amazonaws.com/kobiton-devvn/apps-test/UIKitCatalog-Test-Adhoc.ipa'
const platformVersion = process.env.KOBITON_SESSION_PLATFORM_VERSION || ''
const browserName = process.env.KOBITON_SESSION_BROWSER_NAME || 'safari'
const groupId = process.env.KOBITON_SESSION_GROUP_ID || ''

const kobitonServerConfig = {
  protocol: 'https',
  host: 'api.kobiton.com',
  auth: `${username}:${apiKey}`
}

const desiredCaps = {
  sessionName:        'Automation test session',
  sessionDescription: 'This is an example for iOS app', 
  deviceOrientation:  deviceOrientation,  
  captureScreenshots: captureScreenshots, 
  deviceGroup:        deviceGroup, 
  platformName:       'iOS',
  browserName: browserName
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

describe('iOS Web sample', () => {

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
    .waitForElementByXPath('//button[@aria-label="Google Search"]')
    .click()
    
    let msg = await driver.title()
    assert.include(msg, 'Kobiton')
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
