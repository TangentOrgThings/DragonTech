// vim :set tabstop=2 shiftwidth=2 sts=2 expandtab smarttab :
/**
 *  WS-100+ Dragon Tech Industrial, Ltd.
 *
 *  Copyright 2017-2018 Brian Aker <brian@tangent.org>, DarwinsDen.com
 *
 *  For device parameter information and images, questions or to provide feedback on this device handler,
 *  please visit:
 *
 *      github.com/TangentOrgThings/ws100plus/
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
 *  Author: Brian Aker <brian@tangent.org>
 *  Date: 2017
 *
 *  Changelog:
 *
 *
<<<<<<< HEAD
=======
 *
>>>>>>> origin/master
 *
 *
 */

def getDriverVersion () {
<<<<<<< HEAD
  return "v5.95"
=======
  return "v5.92"
>>>>>>> origin/master
}

metadata {
  definition (name: "WS-100 Switch", namespace: "TangentOrgThings", author: "brian@tangent.org", ocfDeviceType: "oic.d.switch") {
    capability "Actuator"
<<<<<<< HEAD
    // capability "Health Check"
    capability "Button"
=======
    capability "Health Check"
    capability "Button"
    capability "Illuminance Measurement"
>>>>>>> origin/master
    capability "Indicator"
    capability "Light"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch"

<<<<<<< HEAD
    attribute "DeviceReset", "enum", ["false", "true"]
    attribute "logMessage", "string"        // Important log messages.
    attribute "lastError", "string"        // Last error message

    attribute "invertedState", "enum", ["false", "true"]

    attribute "Lifeline", "string"
    attribute "configured", "enum", ["false", "true"]
=======
    command "tapUp2"
    command "tapDown2"
    command "tapUp3"
    command "tapDown3"
    command "holdUp"
    command "holdDown"

    attribute "Lifeline", "string"
    attribute "configured", "enum", ["false", "true"]
    attribute "reset", "enum", ["false", "true"]
>>>>>>> origin/master
    attribute "driverVersion", "string"
    attribute "firmwareVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "NIF", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"
    
    attribute "invertedStatus", "enum", ["false", "true"]
    
    attribute "recentScene", "string"
    attribute "currentScene", "string"
    attribute "setScene", "enum", ["Set_Scene", "Setting_Scene"]
    
    attribute "Scene", "number"
    attribute "keyAttributes", "number"
    
    attribute "Scene_1", "number"
    attribute "Scene_2", "number"

    attribute "setScene", "enum", ["Set", "Setting"]
    attribute "keyAttributes", "number"

    attribute "Scene", "number"
    attribute "Scene_1", "number"
    attribute "Scene_2", "number"
    
    attribute "SwitchAll", "string"

    // zw:L type:1001 mfr:000C prod:4447 model:3033 ver:5.14 zwv:4.05 lib:03 cc:5E,86,72,5A,85,59,73,25,27,70,2C,2B,5B,7A ccOut:5B role:05 ff:8700 ui:8700
    fingerprint type: "1001", mfr: "0184", prod: "4447", model: "3033", deviceJoinName: "WS-100" // cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B",
    fingerprint type: "1001", mfr: "000C", prod: "4447", model: "3033", deviceJoinName: "HS-WS100+" // cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B",
  }

  // simulator metadata
  simulator {
    status "on":  "command: 2003, payload: FF"
    status "off": "command: 2003, payload: 00"

    // reply messages
    reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
    reply "200100,delay 100,2502": "command: 2503, payload: 00"
  }
  
  preferences {
    input "ledIndicator", "enum", title: "LED Indicator", description: "Turn LED indicator... ", required: false, options: ["off": "When Off", "on": "When On", "never": "Never"]
  }

  preferences {
    input "ledIndicator", "enum", title: "LED Indicator", description: "Turn LED indicator... ", required: false, options: ["When Off", "When On", "Never"]
    input "invertSwitch", "bool", title: "Invert Switch", description: "If you oopsed the switch... ", required: false,  defaultValue: false
    input "disbableDigitalOff", "bool", title: "Disable Digital Off", description: "Disallow digital turn off", required: false
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC"
        attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
      }
      tileAttribute("device.status", key: "SECONDARY_CONTROL") {
        attributeState("default", label:'${currentValue}', unit:"")
      }
    }

<<<<<<< HEAD
=======
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

>>>>>>> origin/master
    standardTile("indicator", "device.indicatorStatus", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
      state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
      state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
    }
    
    valueTile("setScene", "device.setScene", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
      state "Set_Scene", label: '${name}', action:"configScene", nextState: "Setting_Scene"      	
	  state "Setting_Scene", label: '${name}' //, nextState: "Set_Scene"
    }

    valueTile("scene", "device.Scene", width:2, height: 2, decoration: "flat", inactiveLabel: false) {
      state "default", label: '${currentValue}'
    }
    
    valueTile("setScene", "device.setScene", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
      state "Set", label: '${name}', action:"configScene", nextState: "Setting_Scene"
      state "Setting", label: '${name}' //, nextState: "Set_Scene"
    }

    standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
    }

    valueTile("firmwareVersion", "device.firmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: false) {
      state "default", label: '${currentValue}'
    }

    valueTile("driverVersion", "device.driverVersion", width:2, height:2, inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

<<<<<<< HEAD
    standardTile("reset", "device.DeviceReset", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
=======
    valueTile("illuminance", "device.illuminance", width: 2, height: 2) {
      state("illuminance", label:'${currentValue}', unit:"lux", backgroundColors:[
        [value: 8, color: "#767676"],
        [value: 300, color: "#ffa81e"],
        [value: 1000, color: "#fbd41b"]
      ])
    }
    
    standardTile("reset", "device.reset", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
>>>>>>> origin/master
      state "false", label:'', backgroundColor:"#ffffff"
      state "true", label:'reset', backgroundColor:"#e51426"
    }

<<<<<<< HEAD
    main "switch"
    details(["switch", "indicator", "scene", "setScene", "firmwareVersion", "driverVersion", "refresh", "reset"])
=======
    main(["switch"])
    details(["switch", "tapUp2", "tapUp3", "holdUp", "tapDown2", "tapDown3", "holdDown", "indicator", "firmwareVersion", "driverVersion", "setScene", "refresh", "illuminance", "reset"])
>>>>>>> origin/master
  }
}

def getCommandClassVersions() {
  [ 
    0x20: 1,  // Basic
    0x25: 1,  // Switch Binary
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
  ]
}

def parse(String description) {
  def result = null

<<<<<<< HEAD
  //log.debug "PARSE: ${description}"
=======
  log.debug "PARSE: ${description}"
>>>>>>> origin/master
  if (description.startsWith("Err")) {
    createEvent(name: "lastError", value: description, descriptionText: description, isStateChange: true)
    if (description.startsWith("Err 106")) {
      if (state.sec) {
        logger("SECURED: $description", "error")
      } else {
        result = createEvent(
          descriptionText: "This device failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
          eventType: "ALERT",
          name: "secureInclusion",
          value: "failed",
          isStateChange: true,
        )
      }
    }
  } else if (description != "updated") {
<<<<<<< HEAD
    def cmd = zwave.parse(description, getCommandClassVersions())
=======
    def cmd = zwave.parse(description)
>>>>>>> origin/master

    if (cmd) {
      result = zwaveEvent(cmd)

      if (!result) {
        log.warning "Parse Failed and returned ${result} for command ${cmd}"
        result = createEvent(value: description, descriptionText: description, displayed: true)
      } else {
        // If we displayed the result
        // log.debug "zwave.parse() debug: ${description}"
<<<<<<< HEAD
        // logger("Parsed $result")
=======
>>>>>>> origin/master
      }
    } else {
      log.error "zwave.parse() failed: ${description}"
      result = createEvent(value: description, descriptionText: description, displayed: true)
    }
  }

  return result
}

private switchEvents(physicalgraph.zwave.Command cmd, boolean isPhysical = true) {
  if (cmd.value == 254) {
    logger("$device.displayName returned Unknown for status.", "warn")
    return createEvent(descriptionText: "$device.displayName returned Unknown for status.", displayed: true)
  }
  
  return [ createEvent(name: "switch", value: cmd.value ? "on" : "off", type: isPhysical ? "physical" : "digital", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
  return switchEvents(cmd, true);
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  logger("$device.displayName $cmd")
  return switchEvents(cmd, true);
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryGet cmd) {
  log.debug("$device.displayName $cmd")
  return [ createEvent(name: "switch", value: cmd.switchValue ? "on" : "off", type: "physical") ]
=======
  log.debug("$device.displayName $cmd")

  setIlluminance(cmd.value ? 300 : 8)
  [ createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "physical") ]
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  log.debug("$device.displayName $cmd")

  setIlluminance(cmd.value ? 300 : 8)
  [ createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "physical") ]
>>>>>>> origin/master
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug("$device.displayName $cmd")
<<<<<<< HEAD
  return switchEvents(cmd, false);
}

def buttonEvent(button, held, buttonType = "physical") {
=======

  setIlluminance(cmd.value ? 300 : 8)
  [ createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "digital") ]
}

def buttonEvent(button, held, buttonType) {
>>>>>>> origin/master
  log.debug("buttonEvent: $button  held: $held  type: $buttonType")

  button = button as Integer
<<<<<<< HEAD
  String heldType = held ? "held" : "pushed"
  sendEvent(name: "button", value: "$heldType", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
}

// A scene command was received -- it's probably scene 0, so treat it like a button release
def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfGet cmd) {
  log.debug("$device.displayName $cmd")
  buttonEvent(cmd.sceneId, false, "digital")
  [
    createEvent(name: "Scene", value: cmd.sceneId, isStateChange: true, displayed: true),
  ]
=======
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
>>>>>>> origin/master
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfReport cmd) {
  log.debug("$device.displayName $cmd")
<<<<<<< HEAD
  
  // HomeSeer (ST?) does not implement this scene
  if (cmd.sceneId == 0) {
    return [ 
      createEvent(name: "level", value: cmd.level, isStateChange: true, displayed: true),
      createEvent(name: "switch", value: cmd.level == 0 ? "on" : "off", isStateChange: true, displayed: true),
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
  
  [ createEvent(name: "$scene_name", value: cmd.level, isStateChange: true, displayed: true),
    createEvent(name: "Scene", value: cmd.sceneId, isStateChange: true, displayed: true),
  ]
=======

  if (cmd.sceneId == 1) {
    if (cmd.level != 255) {
      return sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, dimmingDuration: 0, level: 255, override: true)])
    }
  } else if (cmd.sceneId == 2) {
    if (cmd.level != 0) {
      return sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, dimmingDuration: 5, level: 0, override: true)])
    }
  }
  
  // state.datascene[cmd.sceneId] = [ level: cmd.level ]
  String scene_name = "Scene_$cmd.sceneId"
  [ createEvent(name: "$scene_name", value: cmd.level, displayed: true) ]
>>>>>>> origin/master
}


def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfGet cmd) {
  log.debug("$device.displayName $cmd")
  
  def Scene = cmd.sceneId
  def Level = cmd.level
  
  sendEvent(name: "recentScene", value: "SceneId: $Scene, level: $Level")
  
  Integer button = ((cmd.sceneId + 1) / 2) as Integer
  Boolean held = !(cmd.sceneId % 2)
  buttonEvent(button, held, "digital")
}


def zwaveEvent(physicalgraph.zwave.commands.sceneactivationv1.SceneActivationSet cmd) {
  log.debug("$device.displayName $cmd")
<<<<<<< HEAD
  Integer set_sceen = ((cmd.sceneId + 1) / 2) as Integer
  buttonEvent(set_sceen, false, "digital")
  [ createEvent(name: "setScene", value: "Setting", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
  logger("$device.displayName $cmd")

  if (cmd.parameterNumber == 3) {
    def value = "when off"
    
=======
  
  Integer button = ((cmd.sceneId + 1) / 2) as Integer
  Boolean held = !(cmd.sceneId % 2)
  buttonEvent(button, held, "digital")
}


def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
  log.debug("$device.displayName $cmd")
  
  if (cmd.parameterNumber == 3) {
    def value = "when off"
>>>>>>> origin/master
    if (cmd.configurationValue[0] == 1) {
      value = "when on"
    }
    if (cmd.configurationValue[0] == 2) {
      value = "never"
    }
<<<<<<< HEAD
    state.indicatorStatus = value
    return [ createEvent(name: "indicatorStatus", value: value, display: false) ]
  } else if (cmd.parameterNumber == 4) {  
    if ( cmd.configurationValue[0] != invertSwitch) {
      return response( [
        zwave.configurationV1.configurationSet(scaledConfigurationValue: invertSwitch ? 1 : 0, parameterNumber: cmd.parameterNumber, size: 1).format(),
        zwave.configurationV1.configurationGet(parameterNumber: cmd.parameterNumber).format(),
      ])
    }

    return [ createEvent(name: "invertedState", value: invertedStatus, display: true) ]
  }

  [ createEvent(descriptionText: "$device.displayName has unknown configuration parameter $cmd.parameterNumber : $cmd.configurationValue[0]", isStateChange: false) ]
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  logger("$device.displayName $cmd")
=======
    [ createEvent(name: "indicatorStatus", value: value, display: false) ]
  } else if (cmd.parameterNumber == 4) {
    [ createEvent(name: "invertedStatus", value: cmd.configurationValue[0] == 1 ? true : false, display: true) ]
  } else {
    [ createEvent(descriptionText: "$device.displayName has unknown configuration parameter $cmd.parameterNumber : $cmd.configurationValue[0]", isStateChange: false) ]
  }
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master

  if ( cmd.manufacturerId == 0x000C ) {
    updateDataValue("manufacturer", "HomeSeer")
    if (! cmd.manufacturerName ) {
      state.manufacturer= "HomeSeer"
    }
  } else if ( cmd.manufacturerId == 0x0184 ) {
    updateDataValue("manufacturer", "Dragon Tech Industrial, Ltd.")
    if (! cmd.manufacturerName ) {
      state.manufacturer= "Dragon Tech Industrial, Ltd."
    }
  } else {
    if ( cmd.manufacturerId == 0x0000 ) {
      cmd.manufacturerId = 0x0184
    }

    updateDataValue("manufacturer", "Unknown Licensed Dragon Tech Industrial, Ltd.")
    state.manufacturer= "Dragon Tech Industrial, Ltd."
  }
<<<<<<< HEAD

  if ( ! state.manufacturer ) {
    state.manufacturer= cmd.manufacturerName
  }

=======
  
  if ( ! state.manufacturer ) {
    state.manufacturer= cmd.manufacturerName
  }
  
>>>>>>> origin/master
  state.manufacturer= cmd.manufacturerName
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
  updateDataValue("manufacturer", "${state.manufacturer}")

  sendEvent(name: "MSR", value: "$msr", descriptionText: "$device.displayName", isStateChange: false)
  [ createEvent(name: "Manufacturer", value: "${state.manufacturer}", descriptionText: "$device.displayName", isStateChange: false) ]
}

def zwaveEvent(physicalgraph.zwave.commands.crc16encapv1.Crc16Encap cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
=======
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master

  def versions = commandClassVersions
  def version = versions[cmd.commandClass as Integer]
  def ccObj = version ? zwave.commandClass(cmd.commandClass, version) : zwave.commandClass(cmd.commandClass)
  def encapsulatedCommand = ccObj?.command(cmd.command)?.parse(cmd.data)
  if (encapsulatedCommand) {
    zwaveEvent(encapsulatedCommand)
  }
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")

=======
  log.debug("$device.displayName $cmd")
  
>>>>>>> origin/master
  def text = "$device.displayName: firmware version: ${cmd.applicationVersion}.${cmd.applicationSubVersion}, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
  state.firmwareVersion = cmd.applicationVersion+'.'+cmd.applicationSubVersion
  [ createEvent(name: "firmwareVersion", value: "V ${state.firmwareVersion}", descriptionText: "$text", displayed: true, isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  updateDataValue("FirmwareMdReport", firmware_report)
  [ createEvent(name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", displayed: true, isStateChange: true) ]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  logger("$device.displayName command not implemented: $cmd", "error")
=======
  log.debug("$device.displayName $cmd")
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  updateDataValue("FirmwareMdReport", firmware_report)
  [createEvent(name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", displayed: true, isStateChange: true)]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  log.error("$device.displayName $cmd")
>>>>>>> origin/master
  [ createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
<<<<<<< HEAD
  logger("$device.displayName command not implemented: $cmd")
=======
  log.debug("$device.displayName command not implemented: $cmd")
>>>>>>> origin/master
  [ createEvent(name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false) ]
}

def on() {
<<<<<<< HEAD
  log.debug("$device.displayName on()")

  state.lastActive = new Date().time
  buttonEvent(1, false, "digital")

  delayBetween([
    zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: 1).format(),
    zwave.switchBinaryV1.switchBinaryGet().format(),
=======
  log.debug "on()"
  setIlluminance(0xFF)
  state.lastActive = new Date().time
  delayBetween([
    zwave.basicV1.basicSet(value: 0xFF).format(),
    zwave.switchBinaryV1.switchBinaryGet().format()
>>>>>>> origin/master
  ])
}

def off() {
<<<<<<< HEAD
  log.debug("$device.displayName off()")

  state.lastActive = new Date().time
  buttonEvent(2, false, "digital")

  if (settings.disbableDigitalOff) {
    logger("..off() disabled")
    return zwave.switchBinaryV1.switchBinaryGet().format()
  }

  delayBetween([
    // zwave.switchBinaryV1.switchBinarySet(switchValue: 0x00).format(),
    zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: 2).format(),
    zwave.switchBinaryV1.switchBinaryGet().format(),
  ])
=======
  log.debug "off()"
  setIlluminance(0x00)
  delayBetween([
    zwave.basicV1.basicSet(value: 0x00).format(),
    zwave.switchBinaryV1.switchBinaryGet().format()
  ])
}

def setIlluminance (level) {
  if (level > 8) {
    sendEvent(name: "illuminance", value: 300, unit: "lux")
  } else {
    sendEvent(name: "illuminance", value: 8, unit: "lux")
  }
>>>>>>> origin/master
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 * */
def ping() {
<<<<<<< HEAD
  log.debug "ping() called (poll to follow)"
  poll()
}

def refresh() {
  logger("refresh() called (poll to follow)")
  response( poll() )
=======
  zwave.switchBinaryV1.switchBinaryGet().format()
}

def refresh() {
  log.debug "refresh() called"
  delayBetween([
    zwave.switchBinaryV1.switchBinaryGet().format(),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 0).format(),
  ])
>>>>>>> origin/master
}

def poll() {
  delayBetween([
    zwave.switchBinaryV1.switchBinaryGet().format(),
  ])
}

void indicatorWhenOn() {
<<<<<<< HEAD
  logger("$device.displayName indicatorWhenOn()")
  // sendEvent(name: "indicatorStatus", value: "when on", displayed: false)

  // Indicate when off
  state.indicatorStatus = "when on"

  sendHubCommand([
    zwave.configurationV1.configurationSet(scaledConfigurationValue: 1, parameterNumber: 3, size: 1).format(),
    zwave.configurationV1.configurationGet(parameterNumber: 3).format(),
  ])
}

void indicatorWhenOff() {
  logger("$device.displayName indicatorWhenOff()")

  state.indicatorStatus = "when off"

  sendHubCommand([
    zwave.configurationV1.configurationSet(scaledConfigurationValue: 0, parameterNumber: 3, size: 1).format(),
    zwave.configurationV1.configurationGet(parameterNumber: 3).format(),
  ])
}

void indicatorNever() {
  logger("$device.displayName indicatorNever()")
  // sendEvent(name: "indicatorStatus", value: "never", displayed: false)

  // Never turn on Indicator
  state.indicatorStatus = "never"

  sendHubCommand([
    zwave.configurationV1.configurationSet(scaledConfigurationValue: 2, parameterNumber: 3, size: 1).format(),
    zwave.configurationV1.configurationGet(parameterNumber: 3).format(),
  ])
=======
  sendEvent(name: "indicatorStatus", value: "when on", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(scaledConfigurationValue: 1, parameterNumber: 3, size: 1).format()))
}

void indicatorWhenOff() {
  sendEvent(name: "indicatorStatus", value: "when off", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(scaledConfigurationValue: 0, parameterNumber: 3, size: 1).format()))
}

void indicatorNever() {
  sendEvent(name: "indicatorStatus", value: "never", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(scaledConfigurationValue: 2, parameterNumber: 3, size: 1).format()))
>>>>>>> origin/master
}

def invertSwitch(invert=true) {
  if (invert) {
    sendCommands([
      zwave.configurationV1.configurationSet(scaledConfigurationValue: 1, parameterNumber: 4, size: 1),
      zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
  else {
    sendCommands([
      zwave.configurationV1.configurationSet(scaledConfigurationValue: 0, parameterNumber: 4, size: 1),
      zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
  state.reset = true
  [ createEvent(name: "DeviceReset", value: state.reset, descriptionText: cmd.toString(), isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.zwavecmdclassv1.NodeInfo cmd) {
  logger("$device.displayName $cmd")
=======
  log.debug("$device.displayName $cmd")
  state.reset = true
  [ createEvent(name: "reset", value: state.reset, descriptionText: cmd.toString(), isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.zwavecmdclassv1.NodeInfo cmd) {
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master
  [ createEvent(name: "NIF", value: "$cmd", descriptionText: "$cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneSupportedReport cmd) {
<<<<<<< HEAD
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

=======
  log.debug("$device.displayName $cmd")
  
  def cmds = []
  
  for (def x = 1; x <= cmd.supportedScenes; x++) {
    cmds << zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: x)
  }
  
  sendCommands(cmds)
  
  [ createEvent(descriptionText: "$cmd", isStateChange: false, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master
  def result = []
  
  state.lastActive = new Date().time

  state.lastActive = new Date().time

  switch (cmd.sceneNumber) {
    case 1:
    // Up
    switch (cmd.keyAttributes) {
      case 1:
<<<<<<< HEAD
      case 2:
      buttonEvent(cmd.sceneNumber, cmd.keyAttributes == 2 ? true : false, "physical")      
      result << response(delayBetween([
                              zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format(),
                             ], 5000))
      case 0:     
      sendEvent(name: "switch", value: "on", type: "physical")
      break;
      case 3:
      // 2 Times
      buttonEvent(3, false, "physical")
      break;
      case 4:
      // 3 Three times
      buttonEvent(5, false, "physical")
      break;
=======
      result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
      buttonEvent(1, false, "physical")
      setIlluminance(0xFF)
      sendEvent(name: "switch", value: "on", type: "physical")
      break
      break
      case 2:
      // Hold
      buttonEvent(cmd.keyAttributes, true, "physical")
      // setIlluminance(0xFF)
      // sendEvent(name: "switch", value: "on", type: "physical")
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      buttonEvent(cmd.keyAttributes, false, "physical")
      break
      case 4:
      // 3 Three times
       buttonEvent(cmd.keyAttributes, false, "physical")
      break
>>>>>>> origin/master
      default:
      log.error ("unexpected up press keyAttribute: $cmd")
    }
    break

    case 2:
    // Down
    switch (cmd.keyAttributes) {
      case 1:
<<<<<<< HEAD
      case 2:
      buttonEvent(cmd.sceneNumber, cmd.keyAttributes == 2 ? true : false, "physical")
      result << response(delayBetween([
                              zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format(),
                             ], 5000))
      case 0:
      sendEvent(name: "switch", value: "on", type: "physical")
      break;
      case 3:
      // 2 Times
      buttonEvent(4, false, "physical")
      break;
      case 4:
      // 3 Three times
      buttonEvent(6, false, "physical")
      break;
      default:
      log.error ("unexpected up press keyAttribute: $cmd")
=======
      result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
       buttonEvent(5, false, "physical")
      setIlluminance(0x00)
      sendEvent(name: "switch", value: "off", type: "physical")
      break
      case 2:
      // Hold
      buttonEvent(cmd.keyAttributes +4, false, "physical")
      // setIlluminance(0x00)
      // sendEvent(name: "switch", value: "off", type: "physical")
      // result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      buttonEvent(cmd.keyAttributes +4, false, "physical")
      break
      case 4:
      // 3 Times
      buttonEvent(cmd.keyAttributes +4, false, "physical")
      break
      default:
      log.debug ("unexpected down press keyAttribute: $cmd.keyAttributes")
>>>>>>> origin/master
    }
    break

    default:
    // unexpected case
    log.debug ("unexpected scene: $cmd.sceneNumber")
  }
<<<<<<< HEAD

  result << createEvent(name: "keyAttributes", value: cmd.keyAttributes, isStateChange: true, displayed: true)
  result << createEvent(name: "Scene", value: cmd.sceneNumber, isStateChange: true, displayed: true)
  
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  logger("$device.displayName $cmd")

  state.groups = cmd.supportedGroupings

  if (cmd.supportedGroupings) {
    def cmds = []
    for (def x = cmd.supportedGroupings; x <= cmd.supportedGroupings; x++) {
      cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: x, listMode: 0x01);
      cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: x);
      cmds << zwave.associationV2.associationGet(groupingIdentifier: x);
    }

=======

  [ 
    createEvent(name: "keyAttributes", value: cmd.keyAttributes, isStateChange: true, displayed: true),
    createEvent(name: "Scene", value: cmd.sceneNumber, isStateChange: true, displayed: true)
  ]
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
  log.debug("$device.displayName $cmd")

  state.groups = cmd.supportedGroupings

  if (cmd.supportedGroupings) {
    def cmds = []
    for (def x = cmd.supportedGroupings; x <= cmd.supportedGroupings; x++) {
      cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: x, listMode: 0x01);
      cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: x);
      cmds << zwave.associationV2.associationGet(groupingIdentifier: x);
    }

>>>>>>> origin/master
    sendCommands(cmds, 2000)
  } else {
    [ createEvent(descriptionText: "$device.displayName reported no groups", isStateChange: true, displayed: true) ]
  }
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
=======
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master
  [ createEvent(descriptionText: "$device.displayName AssociationGroupInfoReport: $cmd", isStateChange: true, displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")
=======
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master
  [ createEvent(descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", displayed: true) ]
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
<<<<<<< HEAD
  logger("$device.displayName $cmd")

=======
  log.debug("$device.displayName $cmd")
>>>>>>> origin/master
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
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/master
}

def prepDevice() {
  [
<<<<<<< HEAD
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    // zwave.configurationV1.configurationGet(parameterNumber: 3),
=======
    zwave.zwaveCmdClassV1.requestNodeInfo(),
    // zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [zwaveHubNodeId]),
    // zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, level: 255, override: true),
    // zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, level: 0, override: true),
    // zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 0),
    // zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 1),
    // zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 2),
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.configurationV1.configurationGet(parameterNumber: 3),
>>>>>>> origin/master
    zwave.configurationV1.configurationGet(parameterNumber: 4),
    zwave.versionV1.versionGet(),
    zwave.firmwareUpdateMdV1.firmwareMdGet(),
    //zwave.associationV2.associationGet(groupingIdentifier: 0x01),
    zwave.associationV2.associationGroupingsGet(),
    zwave.switchBinaryV1.switchBinaryGet(),
    zwave.centralSceneV1.centralSceneSupportedGet(),
<<<<<<< HEAD
    zwave.zwaveCmdClassV1.requestNodeInfo(),
    zwave.switchAllV1.switchAllGet(),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 0x00),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, dimmingDuration: 0, level: 255, override: true),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, dimmingDuration: 0, level: 0, override: true),
=======
>>>>>>> origin/master
  ]
}

def installed() {
  logger("$device.displayName installed()")
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
<<<<<<< HEAD

=======
   
>>>>>>> origin/master
  def zwInfo = getZwaveInfo()
  log.debug("$device.displayName $zwInfo")
  sendEvent(name: "NIF", value: "$zwInfo", isStateChange: true, displayed: true)

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
<<<<<<< HEAD
  // sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  sendEvent(name: "reset", value: false, isStateChange: true, displayed: true)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  indicatorWhenOff()
=======
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])

  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  sendEvent(name: "ledIndicator", value: "when off", displayed: true, isStateChange: true)
>>>>>>> origin/master

  sendCommands( prepDevice(), 2000 )
}

def updated() {
  if (state.updatedDate && (Calendar.getInstance().getTimeInMillis() - state.updatedDate) < 5000 ) {
    return
  }
  state.loggingLevelIDE = 4
  
<<<<<<< HEAD
  if (! state.reset) {
    sendEvent(name: "reset", value: false, isStateChange: true, displayed: true)
  } else {
    sendEvent(name: "reset", value: true, isStateChange: true, displayed: true)
  }

  logger("$device.displayName  updated()")

  /*
  def zwInfo = getZwaveInfo()
  if ( !isNull($zwInfo) ) {
  log.debug("$device.displayName $zwInfo")
  sendEvent(name: "NIF", value: "$zwInfo", isStateChange: true, displayed: true)
  }
   */

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  // sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
=======
  /*
  def zwInfo = getZwaveInfo()
  if ( !isNull($zwInfo) ) {
    log.debug("$device.displayName $zwInfo")
    sendEvent(name: "NIF", value: "$zwInfo", isStateChange: true, displayed: true)
  }
  */

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
>>>>>>> origin/master

  // Check in case the device has been changed
  state.manufacturer = null
  updateDataValue("MSR", null)
  updateDataValue("manufacturer", null)
  sendEvent(name: "numberOfButtons", value: 8, displayed: true, isStateChange: true)
<<<<<<< HEAD
  //sendEvent(name: "indicator", value: "when off", displayed: true, isStateChange: true)

  if (! state.indicatorStatus) {
    settings.indicatorStatus = state.indicatorStatus
  } else {
    settings.indicatorStatus = "when off"
    state.indicatorStatus = settings.indicatorStatus
  }

  switch (settings.indicatorStatus) {
    case "when on":
    indicatorWhenOn()
    break
    case "when off":
    indicatorWhenOff()
    break
    case "never":
    indicatorNever()
    break
    default:
    indicatorWhenOn()
    break
  }

  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)

  sendCommands( prepDevice(), 2000 )

  // Avoid calling updated() twice
  state.updatedDate = Calendar.getInstance().getTimeInMillis()
=======
  sendEvent(name: "ledIndicator", value: "when off", displayed: true, isStateChange: true)

  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)

  sendCommands( prepDevice(), 2000 )
>>>>>>> origin/master
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
  }
  else if (state.useCrc16) {
    return zwave.crc16EncapV1.crc16Encap().encapsulate(cmd)
  }
  else {
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
private sendCommands(cmds, delay=200) {
  sendHubCommand( cmds.collect{ (it instanceof physicalgraph.zwave.Command ) ? response(encapCommand(it)) : response(it) }, delay)
<<<<<<< HEAD
}
/**
 *  logger()
 *
 *  Wrapper function for all logging:
 *    Logs messages to the IDE (Live Logging), and also keeps a historical log of critical error and warning
 *    messages by sending events for the device's logMessage attribute and lastError attribute.
 *    Configured using configLoggingLevelIDE and configLoggingLevelDevice preferences.
 **/
private logger(msg, level = "debug") {
  switch(level) {
    case "error":
    if (state.loggingLevelIDE >= 1) {
      log.error msg
    }
    if (state.loggingLevelDevice >= 1) {
      sendEvent(name: "lastError", value: "ERROR: ${msg}", displayed: false, isStateChange: true)
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
    if (state.loggingLevelIDE >= 3) log.info msg
      break

    case "debug":
    if (state.loggingLevelIDE >= 4) log.debug msg
      break

    case "trace":
    if (state.loggingLevelIDE >= 5) log.trace msg
      break

    default:
    log.debug msg
    break
  }
=======
>>>>>>> origin/master
}
