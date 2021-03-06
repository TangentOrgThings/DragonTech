// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/**
 *  HomeSeer HS-WD100+
 *
 *  Copyright 2017-2018 Brian Aker <brian@tangent.org>
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
 *  Author: Darwin@DarwinsDen.com
 *  Date: 2016-04-10
 *
 *  Changelog:
 *
 *  0.10 (04/10/2016) - Initial 0.1 Beta.
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
  return "v6.68"
}

metadata {
  definition (name: "WD-100 Dimmer", namespace: "TangentOrgThings", author: "brian@tangent.org", ocfDeviceType: "oic.d.light", executeCommandsLocally: true) {
    capability "Actuator"
    // capability "Health Check"
    capability "Button"
    capability "Light"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch Level"
    capability "Switch"

    attribute "DeviceReset", "enum", ["false", "true"]
    attribute "logMessage", "string"        // Important log messages.
    attribute "lastError", "string"        // Last Error  messages.

    attribute "AssociationGroupings", "number"
    attribute "Lifeline", "string"
    attribute "driverVersion", "string"
    attribute "firmwareVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "FirmwareVersion", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "NIF", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"

    attribute "invertedStatus", "enum", ["false", "true"]
    
    attribute "setScene", "enum", ["Set", "Setting"]
    attribute "keyAttributes", "number"

    attribute "Scene", "number"
    attribute "Scene_1", "number"
    attribute "Scene_1_Duration", "number"
    attribute "Scene_2", "number"
    attribute "Scene_2_Duration", "number"
    
    attribute "SwitchAll", "string"

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
    input "invertSwitch", "bool", title: "Invert Switch", description: "If you oopsed the switch... ", required: false,  defaultValue: false
    input "startPower", "number", title: "Remote Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", range: "1..99", required: false
    input "localStepDuration", "number", title: "Press Configuration button after entering ramp rate preferences\n\nLocal Ramp Rate: Duration of each level (1-255)(1=10ms) [default: 3]", range: "1..255", required: false
    input "localStepSize", "number", title: "Local Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", range: "1..99", required: false
    input "remoteStepDuration", "number", title: "Remote Ramp Rate: Duration of each level (1-255)(1=10ms) [default: 3]", range: "1..255", required: false
    input "remoteStepSize", "number", title: "Remote Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", range: "1..99", required: false
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
    
    valueTile("scene", "device.Scene", width:2, height: 2, decoration: "flat", inactiveLabel: false) {
      state "default", label: '${currentValue}'
    }
    
    valueTile("setScene", "device.setScene", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
      state "Set", label: '${name}', action:"configScene", nextState: "Setting_Scene"
      state "Setting", label: '${name}' //, nextState: "Set_Scene"
    }

    valueTile("firmwareVersion", "device.FirmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: true) {
      state "default", label: '${currentValue}'
    }

    valueTile("driverVersion", "device.driverVersion", inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

    standardTile("reset", "device.DeviceReset", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
      state "false", label:'', backgroundColor:"#ffffff"
      state "true", label:'reset', backgroundColor:"#e51426"
    }
    
    main "switch"
    details(["switch", "scene", "setScene", "firmwareVersion", "driverVersion", "refresh", "reset"])
  }
}

def getCommandClassVersions() {
  [ 
    0x20: 1,  // Basic
    0x26: 3,  // SwitchMultilevel
    0x27: 1,  // Switch All
    0x2B: 1,  // SceneActivation
    0x2C: 1,  // Scene Actuator Conf
    0x59: 1,  // Association Grp Info
    0x5A: 1,  // Device Reset Locally
    0x5B: 1,  // Central Scene
    0x70: 2,  // Configuration
    0x72: 2,  // Manufacturer Specific
    // 0x73: 1, // Powerlevel
    0x7A: 2,  // Firmware Update Md
    0x86: 1,  // Version
    0x85: 2,  // Association	0x85	V1 V2
    0x01: 1,  // Z-wave command class
  ]
}

def parse(String description) {
  def result = null

  //log.debug "PARSE: ${description}"
  if (description.startsWith("Err")) {
    log.error "parse error: ${description}"
    result = []
    result << createEvent(name: "lastError", value: "Error parse() ${description}", descriptionText: description)
    
    if (description.startsWith("Err 106")) {
      result << createEvent(
          descriptionText: "Security, possible key exchange error.",
          eventType: "ALERT",
          name: "secureInclusion",
          value: "failed",
          isStateChange: true,
        )
    }
  } else if (description != "updated") {
    def cmd = zwave.parse(description, getCommandClassVersions())

    if (cmd) {
      result = zwaveEvent(cmd)

      if (! result) {
        log.warning "zwaveEvent() failed to return a value for command ${cmd}"
        result = createEvent(name: "lastError", value: "$cmd", descriptionText: description)
      } else {
        // If we displayed the result
        // log.debug "zwave.parse() debug: ${description}"
        // logger("Parsed $result")
      }
    } else {
      log.warning "zwave.parse() failed for: ${description}"
      result = createEvent(name: "lastError", value: "zwave.parse() failed for: ${description}", descriptionText: description)
    }
  } else {
    result = createEvent(name: "logMessage", value: "DESC: ${description}", descriptionText: description)
  }

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.switchallv1.SwitchAllReport cmd) {
  logger("$device.displayName $cmd")

  state.switchAllModeCache = cmd.mode

  def msg = ""
  switch (cmd.mode) {
    case 0:
    msg = "Device is excluded from the all on/all off functionality."
    break

    case 1:
    msg = "Device is excluded from the all on functionality but not all off."
    break

    case 2:
    msg = "Device is excluded from the all off functionality but not all on."
    break

    default:
    msg = "Device is included in the all on/all off functionality."
    break
  }
  logger("Switch All Mode: ${msg}","info")

  if (cmd.mode != 0) {
    sendCommands([
      zwave.switchAllV1.switchAllSet(mode: 0x00),
      zwave.switchAllV1.switchAllGet(),
    ])
  } else {
    [ 
      createEvent(name: "SwitchAll", value: msg, isStateChange: true, displayed: true),
    ]
  }
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  logger("$device.displayName $cmd")
  dimmerEvents(cmd)
}


def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelSet cmd) {
  logger("$device.displayName $cmd")
  dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  logger("$device.displayName $cmd (duplicate)")
  if (0) {
    dimmerEvents(cmd, true)
  }
  
  [ createEvent(descriptionText: "$device.displayName basic duplicate.", isStateChange: false, displayed: false) ]
}

// physical device events ON/OFF
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  log.debug("$device.displayName $cmd")
  dimmerEvents(cmd)
}

// This should not happen, but it does.
def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  logger("$device.displayName $cmd (duplicate)")
  //dimmerEvents(cmd, true)
   [ createEvent(descriptionText: "$device.displayName basic binary duplicate.") ]
}

def buttonEvent(button, held, buttonType = "physical") {
  log.debug("buttonEvent: $button  held: $held  type: $buttonType")

  button = button as Integer
  String heldType = held ? "held" : "pushed"

  sendEvent(name: "button", value: "$heldType", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
}

// A scene command was received -- it's probably scene 0, so treat it like a button release
def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfGet cmd) {
  log.debug("$device.displayName $cmd")
  // buttonEvent(cmd.sceneId, false, "digital")
  [
    createEvent(name: "Scene", value: cmd.sceneId, isStateChange: true, displayed: true),
  ]
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfReport cmd) {
  log.debug("$device.displayName $cmd")
  
  // HomeSeer (ST?) does not implement this scene
  if (cmd.sceneId == 0) {
    return [
      createEvent(name: "Scene", value: cmd.sceneId, isStateChange: true, displayed: true),
      createEvent(name: "level", value: cmd.level, isStateChange: true, displayed: true),
      createEvent(name: "switch", value: cmd.level == 0 ? "off" : "on", isStateChange: true, displayed: true),
    ]
  }

  if (cmd.sceneId == 1) {
    if (cmd.level != 255) {
      sendCommands([
        zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: cmd.sceneId, dimmingDuration: 0, level: 255, override: true),
      ])
    }
  } else if (cmd.sceneId == 2) {
    if (cmd.level) {
      sendCommands([
        zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: cmd.sceneId, dimmingDuration: 0, level: 0, override: true),
      ])
    }
  }

  String scene_name = "Scene_$cmd.sceneId"
  String scene_duration_name = String.format("Scene_%d_Duration", cmd.sceneId)
  
  [ createEvent(name: "$scene_name", value: cmd.level, isStateChange: true, displayed: true),
    createEvent(name: "$scene_duration_name", value: cmd.dimmingDuration, isStateChange: true, displayed: true),
    createEvent(name: "Scene", value: cmd.sceneId, isStateChange: true, displayed: true),
  ]
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactivationv1.SceneActivationSet cmd) {
  log.debug("$device.displayName $cmd")
  Integer set_sceen = ((cmd.sceneId + 1) / 2) as Integer
  buttonEvent(set_sceen, false, "digital")
  [ createEvent(name: "setScene", value: "Setting", isStateChange: true, displayed: true) ]
}

private dimmerEvents(physicalgraph.zwave.Command cmd, boolean isPhysical = false) {
  if (level > 99 && level < 255) {
    logger("$device.displayName returned Unknown for status.", "warn")
    return [ createEvent(descriptionText: "$device.displayName returned Unknown for status.", displayed: true) ]
  }
  
  def level = cmd.value

  if (level <= 1) { // Some manufactures will 1 to turn on a single LED to represent state, homeseer does not
    if (level == 1) {
      /*
      sendCommands([
        zwave.switchMultilevelV1.switchMultilevelSet(value: 0x00),
      ]) */
    }
    level = 0
  } else if (level > 1 && level <= 30) {  // Make sure we don't burn anything out
    /*
    sendCommands([
      zwave.switchMultilevelV1.switchMultilevelSet(value: 0x31),
    ]) */
    level = 31
  } else if (level == 255) {
    level = 255
  }  else if (level > 99 && level < 255) {
    logger("$device.displayName returned out of bounds level.", "warn")
    level = 99
  }
  
  def result = [ createEvent(name: "switch", value: level ? "on" : "off", type: isPhysical ? "physical" : "digital", isStateChange: true, displayed: true ) ]
  
  // state.lastLevel = cmd.value
  if (level > 1 && level < 100) {
    result << createEvent(name: "level", value: level, unit: "%", isStateChange: true, displayed: true)
  }

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
  log.debug("$device.displayName $cmd")

  log.debug "$device.displayName duration of each level ${cmd.configurationValue[10]}"
  log.debug "$device.displayName orientation ${cmd.configurationValue[4]}"
  log.debug "$device.displayName remote number of levels ${cmd.configurationValue[7]}"
  log.debug "$device.displayName remote duration of each level ${cmd.configurationValue[8]}"
  log.debug "$device.displayName number of levels ${cmd.configurationValue[9]}"

  if (cmd.parameterNumber == 4) {  
    if ( cmd.configurationValue[0] != invertSwitch) {
      return response( [
        zwave.configurationV1.configurationSet(scaledConfigurationValue: invertSwitch ? 1 : 0, parameterNumber: cmd.parameterNumber, size: 1).format(),
        zwave.configurationV1.configurationGet(parameterNumber: cmd.parameterNumber).format(),
      ])
    }

    return [ createEvent(name: "invertedStatus", value: invertedStatus, display: true) ]
  }

  [ createEvent(descriptionText: "$device.displayName ConfigurationReport: $cmd", displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  log.debug("$device.displayName $cmd")

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
  updateDataValue("manufacturerName", cmd.manufacturerName)
  updateDataValue("manufacturerId", manufacturerCode)
  updateDataValue("productId", productCode)
  updateDataValue("productTypeId", productTypeCode)

  sendEvent(name: "MSR", value: "$msr", descriptionText: "$device.displayName", isStateChange: true)
  [ createEvent(name: "Manufacturer", value: "${state.manufacturer}", descriptionText: "$device.displayName", isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.crc16encapv1.Crc16Encap cmd) {
  logger("$device.displayName $cmd")

  def versions = commandClassVersions
  def version = versions[cmd.commandClass as Integer]
  def ccObj = version ? zwave.commandClass(cmd.commandClass, version) : zwave.commandClass(cmd.commandClass)
  def encapsulatedCommand = ccObj?.command(cmd.command)?.parse(cmd.data)
  if (encapsulatedCommand) {
    zwaveEvent(encapsulatedCommand)
  }
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
  logger("$device.displayName $cmd")
  def text = "$device.displayName: firmware version: ${cmd.applicationVersion}.${cmd.applicationSubVersion}, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
  state.firmwareVersion = cmd.applicationVersion+'.'+cmd.applicationSubVersion
  [ createEvent(name: "FirmwareVersion", value: "V ${state.firmwareVersion}", descriptionText: "$text", isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
  logger("$device.displayName $cmd")
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  [ createEvent(name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelStopLevelChange cmd) {
  logger("$device.displayName $cmd")
  [ createEvent(name:"switch", value:"on", isStateChange: true, displayed: true), response(zwave.switchMultilevelV1.switchMultilevelGet().format()) ]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelStopLevelChange cmd) {
  logger("$device.displayName $cmd")
  [ createEvent(name:"switch", value:"on", isStateChange: true, displayed: true), response(zwave.switchMultilevelV1.switchMultilevelGet().format()) ]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  logger("$device.displayName command not implemented: $cmd", "error")
  [ createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
  loggesr("$device.displayName $cmd")
  [ createEvent(name: "hail", value: "hail", descriptionText: "Switch button was pressed", isStateChange: true, displayed: true) ]
}

def on() {
  log.debug("$device.displayName on()")

  state.lastActive = new Date().time
  
  if (0) { // Add option to have digital commands execute buttons
    buttonEvent(1, false, "digital")
  }
  delayBetween([
    zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: 1).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format(),
  ], 5000)
}

def off() {
  log.debug("$device.displayName off()")
  
  state.lastActive = new Date().time
  
  if (0) { // Add option to have digital commands execute buttons
    buttonEvent(2, false, "digital")
  }
  delayBetween([
    zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: 2).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format(),
  ], 5000)
}

def setLevel (value) {
  logger("$device.displayName setLevel() value: $value")
  def valueaux = value as Integer

  def level = (valueaux != 255) ? Math.max(Math.min(valueaux, 99), 33) : 99
  
  if (valueaux <= 1) { // Check for 1 because of bad device on setting non-existant indicator
    level = 0
  } 
  
  // In the future establish if going from on to off
  Integer set_sceen = level ? 1 : 2
  buttonEvent(set_sceen, false, "digital")
  //  zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 2, sceneId: 2).format(),

  sendEvent(name: "level", value: level, unit: "%")

  delayBetween ([
    // zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: 2).format(),
    zwave.switchMultilevelV1.switchMultilevelSet(value: level).format(),
    zwave.switchMultilevelV1.switchMultilevelGet().format(),
  ])
}

def setLevel(value, duration) {
  logger("$device.displayName setLevel( value: $value, duration: $duration )")
  
  /*
  def valueaux = value as Integer

  def level = Math.max(Math.min(valueaux, 99), 0)
  def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
  def getStatusDelay = duration < 128 ? (duration*1000)+2000 : (Math.round(duration / 60)*60*1000)+2000
  */

  return setLevel(value);
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 **/
def ping() {
  logger("$device.displayName ping()")
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def refresh() {
  logger("$device.displayName refresh()")

  response(zwave.switchMultilevelV1.switchMultilevelGet().format())
}

def poll() {
  logger("$device.displayName poll()")
  if (0) {
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 0).format()
  }
  
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
  logger("$device.displayName $cmd")
  state.DeviceReset = true
  [ createEvent(name: "DeviceReset", value: state.reset, descriptionText: cmd.toString(), isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneSupportedReport cmd) {
  logger("$device.displayName $cmd")

  def cmds = []

  for (def x = 1; x <= cmd.supportedScenes; x++) {
    cmds << zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: x)
  }

  sendCommands(cmds)

  [ createEvent(descriptionText:"CentralScene report $cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  logger("$device.displayName $cmd")
  
  if ( cmd.sequenceNumber > 1 && cmd.sequenceNumber < state.sequenceNumber ) {
    return [ createEvent(descriptionText: "Late sequenceNumber  $cmd", isStateChange: false, displayed: true) ]
  }
  state.sequenceNumber= cmd.sequenceNumber
  
  def result = []
  def cmds = []

  state.lastActive = new Date().time

  switch (cmd.sceneNumber) {
    case 1:
    switch (cmd.keyAttributes) {
      case 2:
      case 0:
      buttonEvent(cmd.sceneNumber, cmd.keyAttributes == 0 ? false : true, "physical")
      case 1:
      result << createEvent(name: "switch", value: cmd.sceneNumber == 1 ? "on" : "off", type: "physical")
      break;
      case 3:
      // 2 Times
      buttonEvent(3, false, "physical")
      cmds << zwave.switchMultilevelV1.switchMultilevelSet(value: 99).format()
      break;
      case 4: // 3 Three times
      buttonEvent(5, false, "physical")
      break;
      default:
      log.error ("unexpected up press keyAttribute: $cmd")
      break
    }
    break

    case 2: // Down
    switch (cmd.keyAttributes) {
      case 2:
      case 0:
      buttonEvent(cmd.sceneNumber, cmd.keyAttributes == 0 ? false : true, "physical")
      case 1:
      result << createEvent(name: "switch", value: cmd.sceneNumber == 1 ? "on" : "off", type: "physical")
      break;
      case 3: // 2 Times
      buttonEvent(4, false, "physical")
      break;
      case 4: // 3 Three times
      buttonEvent(6, false, "physical")
      break;
      default:
      log.error ("unexpected up press keyAttribute: $cmd")
    }
    break

    default:
    // unexpected case
    log.error ("unexpected scene: $cmd.sceneNumber")
  }

  result << createEvent(name: "keyAttributes", value: cmd.keyAttributes, isStateChange: true, displayed: true)
  result << createEvent(name: "Scene", value: cmd.sceneNumber, isStateChange: true, displayed: true)
    
  if (0) {
    cmds << zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()
  }
  
  if (cmds.size) {
    result << response(delayBetween(cmds))
  }
  
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  log.debug("$device.displayName $cmd")

  state.groups = cmd.supportedGroupings

  def cmds = []
  if (cmd.supportedGroupings) {
    for (def x = 1; x <= cmd.supportedGroupings; x++) {
      cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: x, listMode: 0x01, refreshCache: true);
      cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: x);
      cmds << zwave.associationV2.associationGet(groupingIdentifier: x);
    }

    sendCommands(cmds, 1000)
  }
  
  [ createEvent(descriptionText: "$device.displayName AssociationGroupingsReport: $cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
  log.debug("$device.displayName $cmd")
  [ createEvent(descriptionText: "$device.displayName AssociationGroupInfoReport: $cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
  log.debug("$device.displayName $cmd")
  [ createEvent(descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
  log.debug("$device.displayName $cmd")
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

def zwaveEvent(physicalgraph.zwave.commands.zwavecmdclassv1.NodeInfo cmd) {
  log.debug("$device.displayName $cmd")
  [ createEvent(name: "NIF", value: "$cmd", descriptionText: "$cmd", isStateChange: true, displayed: true) ]
}

def prepDevice() {
  [
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.versionV1.versionGet(),
    zwave.firmwareUpdateMdV1.firmwareMdGet(),
    zwave.associationV2.associationGroupingsGet(),
    zwave.centralSceneV1.centralSceneSupportedGet(),
    zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, configurationValue: [0]),
    zwave.configurationV1.configurationSet(parameterNumber: 7, size: 1, configurationValue: [1]),
    zwave.configurationV1.configurationSet(parameterNumber: 8, size: 2, configurationValue: [0, 1]),
    zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, configurationValue: [1]),
    zwave.configurationV1.configurationSet(parameterNumber: 10, size: 2, configurationValue: [0, 1]),
    // zwave.configurationV1.configurationGet(parameterNumber: 4),
    // zwave.configurationV1.configurationGet(parameterNumber: 7),
    // zwave.configurationV1.configurationGet(parameterNumber: 8),
    // zwave.configurationV1.configurationGet(parameterNumber: 9),
    // zwave.configurationV1.configurationGet(parameterNumber: 10),
    zwave.zwaveCmdClassV1.requestNodeInfo(),
    zwave.switchAllV1.switchAllGet(),
    // zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 0x00),
    // zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, dimmingDuration: 0, level: 255, override: true),
    // zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, dimmingDuration: 0, level: 0, override: true),
  ]
}

def installed() {
  log.debug "$device.displayName installed()"
  state.loggingLevelIDE = 4
  /*

  def zwInfo = getZwaveInfo()
  if ($zwInfo) {
  log.debug("$device.displayName $zwInfo")
  sendEvent(name: "NIF", value: "$zwInfo", isStateChange: true, displayed: true)
  }
   */

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  // sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID]) //, offlinePingable: "1"])

  // Set Button Number and driver version
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)

  sendCommands( prepDevice() + setDimRatePrefs(), 2000 )
}

def setDimRatePrefs() {
  def cmds = []
/*
  if (remoteStepSize) {
    def remoteStepSize = Math.max(Math.min(remoteStepSize, 99), 1)
    cmds << zwave.configurationV1.configurationSet(scaledConfigurationValue: remoteStepSize, parameterNumber: 7, size: 1)
  }

  if (remoteStepDuration) {
    def remoteStepDuration = Math.max(Math.min(remoteStepDuration, 255), 1)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0, remoteStepDuration], parameterNumber: 8, size: 2)
  }


  if (localStepSize) {
    def localStepSize = Math.max(Math.min(localStepSize, 99), 1)
    cmds << zwave.configurationV2.configurationSet(scaledConfigurationValue: localStepSize, parameterNumber: 9, size: 1)
  }

  if (localStepDuration) {
    def localStepDuration = Math.max(Math.min(localStepDuration, 255), 1)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0,localStepDuration], parameterNumber: 10, size: 2)
  }
*/
  return cmds
}

def updated() {
  state.loggingLevelIDE = 4
  if ( state.updatedDate && ((Calendar.getInstance().getTimeInMillis() - state.updatedDate)) < 5000 ) {
    return
  }
  
  logger("$device.displayName updated()")
  state.loggingLevelIDE = 4

  if (0) {
    def zwInfo = getZwaveInfo()
    if ($zwInfo) {
      log.debug("$device.displayName $zwInfo")
 //   sendEvent(name: "NIF", value: "$zwInfo", isStateChange: true, displayed: true)
    }
  }

  // Check in case the device has been changed
  updateDataValue("MSR", null)

  // Set Button Number and driver version
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  state.driverVersion = getDriverVersion()

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  // sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])

  // sendCommands( prepDevice() + setDimRatePrefs(), 2000 )
  sendCommands( prepDevice(), 2000 )

  // Avoid calling updated() twice
  state.updatedDate = Calendar.getInstance().getTimeInMillis()
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

/**
 *  logger()
 *
 *  Wrapper function for all logging:
 *    Logs messages to the IDE (Live Logging), and also keeps a historical log of critical error and warning
 *    messages by sending events for the device's logMessage attribute.
 *    Configured using configLoggingLevelIDE and configLoggingLevelDevice preferences.
 **/
private logger(msg, level = "debug") {
  switch(level) {
    case "error":
    if (state.loggingLevelIDE >= 1) {
      log.error msg
    }
    if (state.loggingLevelDevice >= 1) {
      sendEvent(name: "lastError", value: "ERROR: ${msg}", displayed: true, isStateChange: true)
    }
    break

    case "warn":
    if (state.loggingLevelIDE >= 2) {
      log.warn msg
    }
    if (state.loggingLevelDevice >= 2) {
      sendEvent(name: "logMessage", value: "WARNING: ${msg}", displayed: false, isStateChange: true)
    }
    break

    case "info":
    if (state.loggingLevelIDE >= 3) {
      log.info msg
    }
    break

    case "debug":
    if (state.loggingLevelIDE >= 4)
    {
      log.debug msg
    }
    break

    case "trace":
    if (state.loggingLevelIDE >= 5) {
      log.trace msg
    }
    break

    default:
    log.debug msg
    break
  }
}
