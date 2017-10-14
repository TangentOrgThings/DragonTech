// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/**
 *  HomeSeer HS-WD100+
 *
 *  Copyright 2016 DarwinsDen.com
 *
 *  For device parameter information and images, questions or to provide feedback on this device handler,
 *  please visit:
 *
 *      darwinsden.com/homeseer100plus/
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Author: Darwin@DarwinsDen.com
 *	Date: 2016-04-10
 *
 *	Changelog:
 *
 *	0.10 (04/10/2016) -	Initial 0.1 Beta.
 *  0.11 (05/28/2016) - Set numberOfButtons attribute for ease of use with CoRE and other SmartApps. Corrected physical/digital states.
 *  0.12 (06/03/2016) - Added press type indicator to display last tap/hold press status
 *  0.13 (06/13/2016) - Added dim level ramp-up option for remote dim commands
 *  0.14 (08/01/2016) - Corrected 60% dim rate limit test that was inadvertently pulled into repository
 *  0.15 (09/06/2016) - Added Firmware version info. Removed unused lit indicator button.
 *  0.16 (09/24/2016) - Added double-tap-up to full brightness option and support for firmware dim rate configuration parameters.
 *  0.17 (10/05/2016) - Added single-tap-up to full brightness option.
 *
 */


def getDriverVersion() {
  return "v5.68"
}

metadata {
  definition (name: "WD-100 Dimmer", namespace: "TangentOrgThings", author: "brian@tangent.org", ocfDeviceType: "oic.d.light") {
    capability "Actuator"
    capability "Configuration"
    capability "Health Check"
    capability "Button"
    capability "Illuminance Measurement"
    capability "Light"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch Level"
    capability "Switch"

    command "tapUp2"
    command "tapDown2"
    command "tapUp3"
    command "tapDown3"
    command "holdUp"
    command "holdDown"

    attribute "reset", "enum", ["false", "true"]
    attribute "Lifeline", "string"
    attribute "driverVersion", "string"
    attribute "firmwareVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "firmwareVersion", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "NIF", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"

    // 0 0 0x2001 0 0 0 a 0x30 0x71 0x72 0x86 0x85 0x84 0x80 0x70 0xEF 0x20
    // zw:L type:1101 mfr:0184 prod:4447 model:3034 ver:5.14 zwv:4.24 lib:03 cc:5E,86,72,5A,85,59,73,26,27,70,2C,2B,5B,7A ccOut:5B role:05 ff:8600 ui:8600
    fingerprint type: "1101", mfr: "000C", prod: "4447", model: "3034", deviceJoinName: "HS-WD100+ In-Wall Dimmer" //, cc: "5E, 86, 72, 5A, 85, 59, 73, 26, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "HS-WD100+ In-Wall Dimmer"
    fingerprint type: "1101", mfr: "0184", prod: "4447", model: "3034", deviceJoinName: "WD100+ In-Wall Dimmer" // , cc: "5E, 86, 72, 5A, 85, 59, 73, 26, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "WD100+ In-Wall Dimmer"
  }

  simulator {
    status "on":  "command: 2003, payload: FF"
    status "off": "command: 2003, payload: 00"
    status "09%": "command: 2003, payload: 09"
    status "10%": "command: 2003, payload: 0A"
    status "33%": "command: 2003, payload: 21"
    status "66%": "command: 2003, payload: 42"
    status "99%": "command: 2003, payload: 63"

    // reply messages
    reply "2001FF,delay 5000,2602": "command: 2603, payload: FF"
    reply "200100,delay 5000,2602": "command: 2603, payload: 00"
    reply "200119,delay 5000,2602": "command: 2603, payload: 19"
    reply "200132,delay 5000,2602": "command: 2603, payload: 32"
    reply "20014B,delay 5000,2602": "command: 2603, payload: 4B"
    reply "200163,delay 5000,2602": "command: 2603, payload: 63"
  }

  preferences {
    input "reverseSwitch", "bool", title: "Reverse Switch",  defaultValue: false,  displayDuringSetup: true, required: false

    input ( "localStepDuration", "number", title: "Press Configuration button after entering ramp rate preferences\n\nLocal Ramp Rate: Duration of each level (1-22)(1=10ms) [default: 3]", defaultValue: 3,range: "1..22", required: false)
    input ( "localStepSize", "number", title: "Local Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", defaultValue: 1, range: "1..99", required: false)
    input ( "remoteStepDuration", "number", title: "Remote Ramp Rate: Duration of each level (1-22)(1=10ms) [default: 3]", defaultValue: 3,range: "1..22", required: false)
    input ( "remoteStepSize", "number", title: "Remote Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", defaultValue: 1, range: "1..99", required: false)
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState:"turningOff"
        attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState:"turningOn"
        attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState:"turningOff"
        attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState:"turningOn"
      }
      tileAttribute ("device.level", key: "SLIDER_CONTROL") {
        attributeState "level", action:"switch level.setLevel"
      }
      tileAttribute("device.status", key: "SECONDARY_CONTROL") {
        attributeState("default", label:'${currentValue}', unit:"")
      }
    }

    standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
    }

    standardTile("configure", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"configuration.configure", icon:"st.secondary.configure"
    }

    valueTile("firmwareVersion", "device.firmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: true) {
      state "default", label: '${currentValue}'
    }

    valueTile("driverVersion", "device.driverVersion", inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

    valueTile("level", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
      state "level", label:'${currentValue} %', unit:"%", backgroundColor:"#ffffff"
    }

    standardTile("tapUp2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲", backgroundColor: "#ffffff", action: "tapUp2", icon: "st.Home.home30"
    }

    standardTile("tapDown2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼", backgroundColor: "#ffffff", action: "tapDown2", icon: "st.Home.home30"
    }

    standardTile("tapUp3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲▲", backgroundColor: "#ffffff", action: "tapUp3", icon: "st.Home.home30"
    }

    standardTile("tapDown3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼▼", backgroundColor: "#ffffff", action: "tapDown3", icon: "st.Home.home30"
    }

    standardTile("holdUp", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▲", backgroundColor: "#ffffff", action: "holdUp", icon: "st.Home.home30"
    }

    standardTile("holdDown", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▼", backgroundColor: "#ffffff", action: "holdDown", icon: "st.Home.home30"
    }

    valueTile("illuminance", "device.illuminance", width: 2, height: 2) {
      state("illuminance", label:'${currentValue}', unit:"lux", backgroundColors:[
        [value: 8, color: "#767676"],
        [value: 300, color: "#ffa81e"],
        [value: 1000, color: "#fbd41b"]
      ]
      )
    }

    main(["switch"])
    details(["switch", "tapUp2", "tapUp3", "holdUp", "tapDown2", "tapDown3", "holdDown", "level", "driverVersion", "refresh", "configure", "illuminance"])
  }
}

def parse(String description) {
  def result = null

  // log.debug "PARSE: ${description}"
  if (description.startsWith("Err")) {
    if (description.startsWith("Err 106")) {
      if (state.sec) {
        log.debug description
      } else {
        result = createEvent(
          descriptionText: "This device failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
          eventType: "ALERT",
          name: "secureInclusion",
          value: "failed",
          isStateChange: true,
        )
      }
    } else {
      result = createEvent(value: description, descriptionText: description)
    }
  } else if (description != "updated") {
    def cmd = zwave.parse(description)

    if (cmd) {
      result = zwaveEvent(cmd)

      if (!result) {
        log.warning "Parse Failed and returned ${result} for command ${cmd}"
        result = createEvent(value: description, descriptionText: description)
      } else {
        // If we displayed the result
      }
    } else {
      log.info "zwave.parse() failed: ${description}"
      result = createEvent(value: description, descriptionText: description)
    }
  }

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
  log.debug("SwitchMultilevelReport() $cmd")
  dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  log.debug("SwitchMultilevelReportV3() $cmd")
  dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelSet cmd) {
  log.debug("SwitchMultilevelSet() $cmd")
  dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug("BasicReport() $cmd")
  dimmerEvents(cmd)
}

// physical device events ON/OFF
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  log.debug("BasicSet() $cmd")
  dimmerEvents(cmd)
}

def buttonEvent(button, held, buttonType) {
  log.debug("buttonEvent: $button  held: $held  type: $buttonType")
  button = button as Integer
  if (held) {
    createEvent(name: "button", value: "held", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
  } else {
    createEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
  }
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfReport cmd) {
  log.debug "SceneActuatorConfReport $cmd"

  if (cmd.sceneId == 1) {
    if (cmd.level != 255) {
      return sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, dimmingDuration: 0, level: 255, override: true)])
    }
  } else if (cmd.sceneId == 2) {
    if (cmd.level != 0) {
      return sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, dimmingDuration: 5, level: 0, override: true)])
    }
  }

  [createEvent(descriptionText: "$device.displayName ConfigurationReport: $cmd", displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactivationv1.SceneActivationSet cmd) {
  log.debug("SceneActivationSet: $cmd")
  Integer button = ((cmd.sceneId + 1) / 2) as Integer
  Boolean held = !(cmd.sceneId % 2)
  buttonEvent(button, held, "digital")
}

private dimmerEvents(physicalgraph.zwave.Command cmd) {
  def value = (cmd.value ? "on" : "off")

  def result = [createEvent(name: "switch", value: value)]
  // state.lastLevel = cmd.value
  if (cmd.value && cmd.value <= 100) {
    result << createEvent(name: "level", value: cmd.value, unit: "%")
  }

  if (cmd.value) {
    result << createEvent(name: "illuminance", value: 300, unit: "lux")
  } else {
    result << createEvent(name: "illuminance", value: 8, unit: "lux")
  }

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
  log.debug "ConfigurationReport $cmd"

  log.debug "$device.displayName duration of each level ${cmd.configurationValue[10]}"
  log.debug "$device.displayName orientation ${cmd.configurationValue[4]}"
  log.debug "$device.displayName remote number of levels ${cmd.configurationValue[7]}"
  log.debug "$device.displayName remote duration of each level ${cmd.configurationValue[8]}"
  log.debug "$device.displayName number of levels ${cmd.configurationValue[9]}"

  [createEvent(descriptionText: "$device.displayName ConfigurationReport: $cmd", displayed: true)]
  /*
  def value = "when off"
  if (cmd.configurationValue[0] == 1) { value = "when on" }
  if (cmd.configurationValue[0] == 2) { value = "never" }

  createEvent(name: "indicatorStatus", value: value)
   */
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  log.debug("ManufacturerSpecificReport: $cmd");

  if ( cmd.manufacturerId == 0x000C ) {
    state.manufacturer= "HomeSeer"
  } else if ( cmd.manufacturerId == 0x0184 ) {
    state.manufacturer= "Dragon Tech Industrial, Ltd."
  } else {
    if ( cmd.manufacturerId == 0x0000 ) {
      cmd.manufacturerId = 0x0184
    }

    state.manufacturer= "Unknown Licensed Dragon Tech Industrial, Ltd."
  }
  state.manufacturer= cmd.manufacturerName
  updateDataValue("manufacturer", state.manufacturer)
  
  state.manufacturerId = cmd.manufacturerId
  state.productTypeId = cmd.productTypeId
  state.productId= cmd.productId

  def manufacturerCode = String.format("%04X", cmd.manufacturerId)
  def productTypeCode = String.format("%04X", cmd.productTypeId)
  def productCode = String.format("%04X", cmd.productId)
  def wirelessConfig = "ZWP"

  sendEvent(name: "ManufacturerCode", value: manufacturerCode)
  sendEvent(name: "ProduceTypeCode", value: productTypeCode)
  sendEvent(name: "ProductCode", value: productCode)
  sendEvent(name: "WirelessConfig", value: wirelessConfig)

  def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
  updateDataValue("MSR", msr)

  sendEvent(name: "MSR", value: "$msr", descriptionText: "$device.displayName", isStateChange: false)
  [ createEvent(name: "Manufacturer", value: "${state.manufacturer}", descriptionText: "$device.displayName", isStateChange: false) ]
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
  def text = "$device.displayName: firmware version: ${cmd.applicationVersion}.${cmd.applicationSubVersion}, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
  state.firmwareVersion = cmd.applicationVersion+'.'+cmd.applicationSubVersion
  [ createEvent(name: "firmwareVersion", value: "V ${state.firmwareVersion}", descriptionText: "$text", isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  [ createEvent(name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelStopLevelChange cmd) {
  [ createEvent(name:"switch", value:"on"), response(zwave.switchMultilevelV1.switchMultilevelGet().format()) ]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelStopLevelChange cmd) {
  [ createEvent(name:"switch", value:"on"), response(zwave.switchMultilevelV1.switchMultilevelGet().format()) ]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  log.error("$device.displayName command not implemented: $cmd")
  [createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
  [name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false]
}

def on() {
  log.debug "on()"
  setIlluminance(0xFF)
  state.lastActive = new Date().time
  delayBetween([
    zwave.basicV1.basicSet(value: 0xFF).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format()
  ], 5000)
}

def off() {
  log.debug "off()"
  setIlluminance(0x00)
  delayBetween([
    zwave.basicV1.basicSet(value: 0x00).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format()
  ], 5000)
}

def setIlluminance (level) {
  if (level > 0) {
    sendEvent(name: "illuminance", value: 300, unit: "lux")
  } else {
    sendEvent(name: "illuminance", value: 8, unit: "lux")
  }
}

def setLevel (value) {
  log.info "setLevel >> value: $value"
  def valueaux = value as Integer
  def level = Math.max(Math.min(valueaux, 99), 0)

  if (level > 0) {
    sendEvent(name: "switch", value: "on")
  } else {
    sendEvent(name: "switch", value: "off")
  }
  setIlluminance(level)
 
  delayBetween ([
   // zwave.basicV1.basicSet(value: level).format(),
    zwave.switchMultilevelV2.switchMultilevelSet(value: level).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format()
  ], 5000)
}

def setLevel(value, duration) {
  log.debug "setLevel >> value: $value, duration: $duration"
  def valueaux = value as Integer
  def level = Math.max(Math.min(valueaux, 99), 0)
  def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
  def getStatusDelay = duration < 128 ? (duration*1000)+2000 : (Math.round(duration / 60)*60*1000)+2000
  

  if (level > 0) {
    sendEvent(name: "switch", value: "on")
  } else {
    sendEvent(name: "switch", value: "off")
  }
  setIlluminance(level)
  
  delayBetween ([
    zwave.switchMultilevelV2.switchMultilevelSet(value: level, dimmingDuration: dimmingDuration).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format()
  ], getStatusDelay)
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 **/
def ping() {
  zwave.switchBinaryV1.switchBinaryGet().format()
}

def refresh() {
  delayBetween([
    zwave.switchMultilevelV1.switchMultilevelGet().format(),
    zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
  ])
}

def poll() {
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
  state.reset = true
  [createEvent(name: "reset", value: state.reset, descriptionText: cmd.toString(), isStateChange: true, displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug("sceneNumber: ${cmd.sceneNumber} keyAttributes: ${cmd.keyAttributes}  rest: $cmd")
  def result = []

  switch (cmd.sceneNumber) {
    case 1:
    // Up
    switch (cmd.keyAttributes) {
      case 1:
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
      result << buttonEvent(7, false, "physical")
      setIlluminance(0xFF)
      sendEvent(name: "switch", value: "on", type: "physical")
      break
      case 2:
      // Hold
      result << buttonEvent(1, true, "physical")
      // setIlluminance(0xFF)
      // sendEvent(name: "switch", value: "on", type: "physical")
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      result << buttonEvent(2, false, "physical")
      break
      case 4:
      // 3 Three times
      result << buttonEvent(3, false, "physical")
      break
      default:
      log.error ("unexpected up press keyAttribute: $cmd")
    }
    break

    case 2:
    // Down
    switch (cmd.keyAttributes) {
      case 1:
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
      result << buttonEvent(8, false, "physical")
      setIlluminance(0x00)
      sendEvent(name: "switch", value: "off", type: "physical")
      break
      case 2:
      // Hold
      result << buttonEvent(4, false, "physical")
      // setIlluminance(0x00)
      // sendEvent(name: "switch", value: "off", type: "physical")
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      result << buttonEvent(5, false, "physical")
      break
      case 4:
      // 3 Times
      result << buttonEvent(6, false, "physical")
      break
      default:
      log.error ("unexpected down press keyAttribute: $cmd.keyAttributes")
    }
    break

    default:
    // unexpected case
    log.error ("unexpected scene: $cmd.sceneNumber")
  }

  return result
}

def tapUp1Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "7"], descriptionText: "$device.displayName Tap-Up-1 (button 7) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown1Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "8"], descriptionText: "$device.displayName Tap-Down-1 (button 8) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp2Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "3"], descriptionText: "$device.displayName Tap-Up-2 (button 3) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown2Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "4"], descriptionText: "$device.displayName Tap-Down-2 (button 4) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "5"], descriptionText: "$device.displayName Tap-Up-3 (button 5) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▼▼▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "6"], descriptionText: "$device.displayName Tap-Down-3 (button 6) pressed", isStateChange: true, type: "$buttonType")
}

def holdUpResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▲")
  sendEvent(name: "button", value: "held", data: [buttonNumber: "7"], descriptionText: "$device.displayName Hold-Up (button 7) pressed", isStateChange: true, type: "$buttonType")
}

def holdDownResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▼")
  sendEvent(name: "button", value: "held", data: [buttonNumber: "8"], descriptionText: "$device.displayName Hold-Down (button 8) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp2() {
  tapUp2Response("digital")
}

def tapDown2() {
  tapDown2Response("digital")
}

def tapUp3() {
  tapUp3Response("digital")
}

def tapDown3() {
  tapDown3Response("digital")
}

def holdUp() {
  holdUpResponse("digital")
}

def holdDown() {
  holdDownResponse("digital")
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  log.debug ("AssociationGroupingsReport: $cmd")

  state.groups = cmd.supportedGroupings

  if (cmd.supportedGroupings) {
    def cmds = []

    cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: 0x01, listMode: 0x01)
    cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: 0x01)

    cmds << zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [0x01])
    cmds << zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [zwaveHubNodeId])
    cmds << zwave.associationV2.associationGet(groupingIdentifier: 0x01)

    if (cmd.supportedGroupings > 1) {
      for (def x = cmd.supportedGroupings +1; x <= cmd.supportedGroupings; x++) {
        cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: x, listMode: 0x01);
        cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: x);
        cmds << zwave.associationV2.associationGet(groupingIdentifier: x);
      }
    }

    sendCommands(cmds, 1000)
  } else {
    [createEvent(descriptionText: "$device.displayName reported no groups", isStateChange: true, displayed: true)]
  }
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
  log.debug ("AssociationGroupInfoReport() $cmd")
  [createEvent(descriptionText: "$device.displayName AssociationGroupInfoReport: $cmd", isStateChange: true, displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
  log.debug "AssociationGroupNameReport() $cmd"
  [createEvent(descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
  log.debug "$device.displayName AssociationReport()"
  Boolean isStateChange
  String event_value
  String event_descriptionText

  // Lifeline
  if (cmd.groupingIdentifier == 0x01) {
    def string_of_assoc = ""
    cmd.nodeId.each {
      string_of_assoc += "${it}, "
    }
    def lengthMinus2 = string_of_assoc.length() - 2
    String final_string = string_of_assoc.getAt(0..lengthMinus2)

    if (cmd.nodeId.any { it == zwaveHubNodeId }) {
      isStateChange = state.isAssociated ?: false
      event_value = "${final_string}"
      event_descriptionText = "${final_string}"
      state.isAssociated = true
    } else {
      isStateChange = state.isAssociated ? true : false
      event_value = ""
      event_descriptionText = "Hub was not found in lifeline: ${final_string}"
      state.isAssociated = false
    }
  } else {
    isStateChange = state.isAssociated ? true : false
    event_value = "misconfigured"
    event_descriptionText = "misconfigured group ${cmd.groupingIdentifier}"
  }

  if (state.isAssociated == false && cmd.groupingIdentifier == 0x01) {
    sendEvent(name: "Lifeline",
      value: "${event_value}",
      descriptionText: "${event_descriptionText}",
      displayed: true,
      isStateChange: isStateChange)
    sendCommands( [ zwave.associationV2.associationSet(groupingIdentifier: cmd.groupingIdentifier, nodeId: [zwaveHubNodeId]) ] )
  } else if (state.isAssociated == true && cmd.groupingIdentifier == 0x01) {
    [ createEvent(name: "Lifeline",
      value: "${event_value}",
      descriptionText: "${event_descriptionText}",
      displayed: true,
      isStateChange: isStateChange) ]
  } else {
    [ createEvent(descriptionText: "$device.displayName is not associated to ${cmd.groupingIdentifier}", displayed: true) ]
  }
}

def prepDevice() {
  [
    zwave.versionV1.versionGet(),
    zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [zwaveHubNodeId]),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, dimmingDuration: 0, level: 255, override: true),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, dimmingDuration: 5, level: 0, override: true),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 1),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 2),
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.versionV1.versionGet(),
    zwave.firmwareUpdateMdV1.firmwareMdGet(),
    zwave.associationV2.associationGet(groupingIdentifier: 0x01),
    zwave.associationV2.associationGroupingsGet(),
    zwave.switchMultilevelV1.switchMultilevelGet(),
    zwave.switchBinaryV1.switchBinaryGet(),
    zwave.zwaveCmdClassV1.requestNodeInfo(),
  ]
}

def configure() {
  log.debug "$device.displayName configure()"
  sendCommands( setDimRatePrefs() )
}

def installed() {
  log.debug "$device.displayName installed()"
  
  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  // Set Button Number and driver version
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)

  sendCommands( prepDevice() + setDimRatePrefs(), 2000 )
}

def setDimRatePrefs() {
  def cmds = []

  if (remoteStepSize) {
    def remoteStepSize = Math.max(Math.min(remoteStepSize, 99), 1)
    cmds << zwave.configurationV2.configurationSet(scaledConfigurationValue: remoteStepSize, parameterNumber: 7, size: 1)
  }

  if (remoteStepDuration) {
    def remoteStepDuration = Math.max(Math.min(remoteStepDuration, 22), 2)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0, remoteStepDuration], parameterNumber: 8, size: 2)
  }


  if (localStepSize) {
    def localStepSize = Math.max(Math.min(localStepSize, 99), 1)
    cmds << zwave.configurationV2.configurationSet(scaledConfigurationValue: localStepSize, parameterNumber: 9, size: 1)
  }

  if (localStepDuration) {
    def localStepDuration = Math.max(Math.min(localStepDuration, 22), 2)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0,localStepDuration], parameterNumber: 10, size: 2)
  }

  if (reverseSwitch) {
    cmds << zwave.configurationV2.configurationSet(scaledConfigurationValue: 1, parameterNumber: 4, size: 1)
  } else {
    cmds << zwave.configurationV2.configurationSet(scaledConfigurationValue: 0, parameterNumber: 4, size: 1)
  }

  return cmds
}

def updated() {
  log.debug "$device.displayName updated()"

  // Check in case the device has been changed
  state.manufacturer = null
  updateDataValue("MSR", null)
  updateDataValue("manufacturer", null)

  // Set Button Number and driver version
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  state.driverVersion = getDriverVersion()

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
  
  sendCommands( prepDevice() + configure(), 2000 )
}

/*****************************************************************************************************************
 *  Private Helper Functions:
 *****************************************************************************************************************/

/**
 *  encapCommand(cmd)
 *
 *  Applies security or CRC16 encapsulation to a command as needed.
 *  Returns a physicalgraph.zwave.Command.
 **/
private encapCommand(physicalgraph.zwave.Command cmd) {
  if (state.sec) {
    return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd)
  } else if (state.useCrc16) {
    return zwave.crc16EncapV1.crc16Encap().encapsulate(cmd)
  } else {
    return cmd
  }
}

/**
 *  prepCommands(cmds, delay=200)
 *
 *  Converts a list of commands (and delays) into a HubMultiAction object, suitable for returning via parse().
 *  Uses encapCommand() to apply security or CRC16 encapsulation as needed.
 **/
private prepCommands(cmds, delay) {
  return response(delayBetween(cmds.collect{ (it instanceof physicalgraph.zwave.Command ) ? encapCommand(it).format() : it }, delay))
}

/**
 *  sendCommands(cmds, delay=200)
 *
 *  Sends a list of commands directly to the device using sendHubCommand.
 *  Uses encapCommand() to apply security or CRC16 encapsulation as needed.
 **/
private sendCommands(cmds, delay=1000) {
  sendHubCommand( cmds.collect{ (it instanceof physicalgraph.zwave.Command ) ? response(encapCommand(it)) : response(it) }, delay)
}
